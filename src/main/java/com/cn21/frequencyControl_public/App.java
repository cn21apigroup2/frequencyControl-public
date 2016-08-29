package com.cn21.frequencyControl_public;

import java.io.IOException;

import com.cn21.data.admin.DataManager;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	System.out.println(App.class.getResource("/"));
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
        dm.onEnd();
    }
}
