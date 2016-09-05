package com.cn21.data.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Administrator
 *
 */
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
					handler.notifySendMessage(MessageRule.APILIMITED_UPDATE);
				else sockets.remove(appKey);
			}
		}
	}
	
	/**
	 * 判断app是否存在
	 * @param appKey
	 * @return
	 */
	public boolean invalidApp(String appKey,String appSecret){
		if(appKey==null||appSecret==null)  return false;
		return true;
	}
	
	@Override
	public void run() {
		while(running){
			Socket socket=null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("accept exception maybe serverSocket close");
				//e.printStackTrace();
				break;
			}
			try{
				BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//获取appkey
				String appKey=in.readLine();
				String appSecret=in.readLine();
				// appkey  invalid
				if(!invalidApp(appKey,appSecret))  continue;
				//使用handleThread 管理连接
				if(appKey!=null&&appSecret!=null){
					System.out.println("server get appkey "+appKey);
					HandleThread handler=new HandleThread(socket);
					sockets.put(appKey, handler);
					handler.start();
				}else{
					System.out.println("server get appkey null");
					socket.close();
				}
			}catch(IOException e){
				System.out.println("accept exception");
				e.printStackTrace();
				break;
			}
		}
		System.out.println("server run end");
	}
	
	public void stopRunning(){
		running=false;
		this.interrupt();
		for(String key:sockets.keySet()){
			HandleThread item=sockets.get(key);
			item.stopRunning();
		}
		/*try {
			join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
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
