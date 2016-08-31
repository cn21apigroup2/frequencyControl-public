package com.cn21.data.socket;

import java.io.IOException;
import java.net.UnknownHostException;

import com.cn21.data.admin.DataSync;
import com.cn21.data.socket.ClientThread.DisconnectListener;

public class Client {
	private long connectPeriod=10000;
	private ClientThread clientThread;
	private String appKey;
	private String appSecret;
	private DataSync sync;
	private boolean running=true;
	private Thread reconnectThread;
	
	public Client(String appKey, String appSecret, DataSync sync){
		this.appKey=appKey;
		this.appSecret=appSecret;
		this.sync=sync;
		clientThread=new ClientThread(appKey, appSecret, sync);
		clientThread.setListener(listener);
	}
	
	public void start(){
		clientThread.start();
	}
	
	public boolean isConnected(){
		return clientThread.isConnected;
	}
	
	public void stopRunning(){
		clientThread.stopRunning();
		running=false;
		if(reconnectThread!=null)
		{
			reconnectThread.interrupt();
			try {
				reconnectThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void close(){
		if(running) stopRunning();
		clientThread.close();
	}
	
	private DisconnectListener listener=new DisconnectListener() {
		
		public void disconnect() {
			clientThread.close();
			reconnectThread=new Thread(){
				@Override
				public void run() {
					while(running){
						try {
							clientThread=new ClientThread(appKey, appSecret, sync);
							clientThread.connect();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("reconnect fail "+e.getMessage());
							e.printStackTrace();
						}
						if(clientThread.isConnected) {
							System.out.println("reconnect success ");
							clientThread.start();
							break;
						}
						try {
							Thread.sleep(connectPeriod);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							break;
						}
					}			
				}
			};
			reconnectThread.start();
		}
	};
}
