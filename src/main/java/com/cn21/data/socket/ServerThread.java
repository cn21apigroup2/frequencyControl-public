package com.cn21.data.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerThread extends Thread{
	public static int PORT=8800;
	
	private Map<String,HandleThread> sockets;
	private ServerSocket serverSocket;
	private boolean running=true;
	
	public ServerThread() throws IOException{
		serverSocket=new ServerSocket(PORT);
		sockets=new HashMap<String,HandleThread>();
	}
	
	public void notifyPullApiLimited(String appKey){
		if(sockets.containsKey(appKey)){
			HandleThread handler=sockets.get(appKey);
			if(handler!=null){
				if(!handler.isClosed())
					handler.notify(MessageRule.APILIMITED_UPDATE);
				else sockets.remove(appKey);
			}
		}
	}
	
	@Override
	public void run() {
		while(running){
			Socket socket=null;
			try {
				socket = serverSocket.accept();
				BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//获取appkey
				String appKey=in.readLine();
				// appkey  invalid
				
				//使用handleThread 管理连接
				if(appKey!=null){
					System.out.println("server get appkey "+appKey);
					HandleThread handler=new HandleThread(socket);
					sockets.put(appKey, handler);
					handler.start();
				}else{
					System.out.println("server get appkey null");
					socket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("server run end");
	}
	
	public void stopRunning(){
		running=false;
		this.interrupt();
	}
	
	public void close(){
		if(running) stopRunning();
		try {
			serverSocket.close();
			for(String key:sockets.keySet()){
				HandleThread item=sockets.get(key);
				item.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
