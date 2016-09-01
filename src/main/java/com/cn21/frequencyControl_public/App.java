package com.cn21.frequencyControl_public;

import java.io.IOException;

import com.cn21.data.admin.DataManager;
import com.cn21.data.socket.Client;
import com.cn21.data.socket.ClientThread;
import com.cn21.data.socket.ServerThread;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	testSocket();
    	/*System.out.println(App.class.getResource("/"));
        System.out.println( "Hello World!" );
        DataManager dm=DataManager.getInstance();
        try {
			dm.init("key","secret");
			System.out.println(dm.getApiInterfaceId("/user/add", null));
			System.out.println(dm.getBlacklistByUsername("xiao"));
			System.out.println(dm.getCurrentTimesByUsername(1, "xiao"));
			System.out.println(dm.addTimesByUsername(1, "xiao"));
			System.out.println(dm.getCurrentTimesByUsername(1, "xiao"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        dm.onEnd();*/ 
		
    }
    
    public static void testSocket(){
    	try {
			ServerThread server=new ServerThread();
			server.start();
			Client client=new Client("111","222", null);
			client.start();
			Thread.sleep(10000);
			//server.notifyPullApiLimited("111");
			server.close();
			Thread.sleep(40000);
			server=new ServerThread();
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
