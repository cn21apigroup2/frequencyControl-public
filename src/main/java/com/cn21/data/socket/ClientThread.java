package com.cn21.data.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.cn21.data.admin.DataSync;
import com.cn21.data.admin.RealApiAdmin;

/**
 * 
 * @author Administrator
 *
 */
public class ClientThread extends Thread{
	private static Logger logger = LogManager.getLogger(ClientThread.class);
	
	public String IP="127.0.0.1";
	public int PORT=8800;
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private boolean running=true;
	private String appKey;
	private String appSecret;
	private DataSync sync;
	private HeartSendThread heartThread;
	public boolean isConnected=false;
	private DisconnectListener listener=null;
	
	public ClientThread(String appKey,String appSecret,DataSync sync){
		if(appKey==null||appSecret==null) throw new IllegalArgumentException("appKey and appSecret cannot be null");
		this.appKey=appKey;
		this.appSecret=appSecret;
		this.sync=sync;
		
	}
	
	public void connect() throws UnknownHostException, IOException{
		logger.info("connect server with socket on "+IP+"："+PORT);
		socket=new Socket(IP, PORT);
		in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out=new PrintWriter(socket.getOutputStream());
		running=true;
		heartThread=new HeartSendThread();
		isConnected=true;
		logger.info("connect server with socket success");
	}
	
	@Override
	public void run() {
		if(socket==null)
			try {
				connect();
			} catch(Exception e1) {
				System.out.println("client connect error "+e1.getMessage());
				isConnected=false;
				if(listener!=null) listener.disconnect();
				return;
			}
		//send appKey
		out.println(appKey);
		out.println(appSecret);
		out.flush();
		heartThread.start();
		//System.out.println("send appkey");                                                             
		while(running){
			try {
				String line=in.readLine();
				handleMessage(Integer.valueOf(line));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				break;
			}catch(NumberFormatException e){
				
			}
		}
		System.out.println("client run end");
	}
	
	private void handleMessage(int value) {
		
		switch(value){
		case MessageRule.APILIMITED_UPDATE:
			logger.info("client recv APILIMITED_UPDATE");
			if(sync!=null){
				sync.pullApiLimitedData();
			}
			break;
		case MessageRule.HEARTACK:
			heartThread.recvAck();
		default:
			break;
		}
	}

	public DisconnectListener getListener() {
		return listener;
	}

	public void setListener(DisconnectListener listener) {
		this.listener = listener;
	}

	public void stopRunning(){
		running=false;
		this.interrupt();
		if(heartThread!=null){
			heartThread.interrupt();		
			try {
				heartThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	
	public void close(){
		if(running) stopRunning();
		try {
			if(socket!=null&&in!=null&&out!=null){
				socket.close();
				in.close();
				out.close();
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int getPORT() {
		return PORT;
	}

	public void setPORT(int pORT) {
		PORT = pORT;
	}
	
	class HeartSendThread extends Thread{
		private long sendPeriod=600000;//10 minutes
		//private long sendPeriod=2000;//test 2s
		private long recvTimeout=60000;//1 minutes
		//private long recvTimeout=10000;  //test 10s
		private boolean isTimeout=true;
		
		@Override
		public void run() {
			isTimeout=true;
			while(running){
				System.out.println("client:send heart");
				out.println(MessageRule.HEART);
				out.flush();
				synchronized(this){
					try {
						wait(recvTimeout);
						if(isTimeout){
							System.out.println("server ack timeout");
							isConnected=false;
							close();
							if(listener!=null)  listener.disconnect();
							break;
						}
						isTimeout=true;
						sleep(sendPeriod);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						break;
					}  
				}
			}
			//System.out.println("client: heartthread run end");
		}
		
		public void recvAck(){
			synchronized(this){
				isTimeout=false;
				System.out.println("client:server ack recv");
				notify();
			}
		}
		
	}
	
	public static interface DisconnectListener{
		void disconnect();
	}

}
