package com.cn21.data.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class HandleThread extends Thread{
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private boolean running=true;
	private int message;
	private Thread heartThread;
	//private Object object=new Object();
	
	public HandleThread(Socket socket) throws IOException{
		this.socket=socket;
		in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out=new PrintWriter(socket.getOutputStream());
		heartThread=new Thread(new HeartRunnable());
	}
	
	@Override
	public void run(){
		heartThread.start();
		while(running){
			try {
				synchronized(this){
					wait();
					out.println(message);
					out.flush();
				}				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try {
			heartThread.join(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("server handle run end");
	}
	
	public void notify(int message){
		synchronized(this){
			this.message=message;
			notify();
		}		
	}
	
	public void stopRunning(){
		running=false;
		this.interrupt();
		heartThread.interrupt();
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
	
	public boolean isClosed(){
		return !running;
	}
	
	private class HeartRunnable implements Runnable{

		public void run() {
			while(running){
				synchronized(this){
					try {
						socket.sendUrgentData(0xFF);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					//out.println(MessageRule.HEART);
					//out.flush();
					//System.out.println("heart"+MessageRule.HEART);
				}
				try {
					//Thread.sleep(1000*60*20);
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			close();
		}
		
	}
}
