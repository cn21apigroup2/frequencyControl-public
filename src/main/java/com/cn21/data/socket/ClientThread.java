package com.cn21.data.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.cn21.data.admin.DataSync;

public class ClientThread extends Thread{
	public static String IP="127.0.0.1";
	public static int PORT=8800;
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private boolean running=true;
	private String appKey;
	private DataSync sync;
	
	public ClientThread(String appKey,DataSync sync) throws UnknownHostException, IOException{
		if(appKey==null) throw new IllegalArgumentException("appKey cannot be null");
		socket=new Socket(IP, PORT);
		in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out=new PrintWriter(socket.getOutputStream());
		this.appKey=appKey;
		this.sync=sync;
	}
	
	@Override
	public void run() {
		//send appKey
		out.println(appKey);
		out.flush();
		//System.out.println("send appkey");                                                             
		while(running){
			try {
				String line=in.readLine();
				handleMessage(Integer.valueOf(line));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}catch(NumberFormatException e){
				
			}
		}
		System.out.println("client run end");
	}
	
	private void handleMessage(int value) {
		
		switch(value){
		case MessageRule.APILIMITED_UPDATE:
			if(sync!=null){
				sync.pullApiLimitedData();
				System.out.println("sync pull");
			}System.out.println("sync pull");
			break;
		default:
			break;
		}
	}

	public void stopRunning(){
		running=false;
		this.interrupt();
	}
	
	public void close(){
		if(running) stopRunning();
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
