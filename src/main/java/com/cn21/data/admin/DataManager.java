package com.cn21.data.admin;

import java.util.Map;

import com.cn21.data.DataAccess;
import com.cn21.module.Blacklist;

public class DataManager implements DataAccess {
	
	private DataManager(){
		
	}
	
	public void init(){
		
	}

	public Blacklist getBlacklistByIp(String ip) {
		// TODO Auto-generated method stub
		return null;
	}

	public Blacklist getBlacklistByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBlacklist(Blacklist blacklist) {
		// TODO Auto-generated method stub

	}

	public void removeBlacklist(Blacklist blacklist) {
		// TODO Auto-generated method stub

	}

	public int getApiInterfaceId(String url, Map<String, String[]> parameters) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getLimitedTimesById(int interface_id) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCurrentTimesByUsername(int interface_id, String username) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCurrentTimesByIp(int interface_id, String ip) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int addTimesByUsername(int interface_id, String username) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int addTimesByIp(int interface_id, String ip) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static DataManager getInstance(){
		return inner.instance;
	}
	
	private static class inner{
		private static DataManager instance=new DataManager();
	}

}
