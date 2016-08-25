package com.cn21.data.admin;

import java.util.Map;

import com.cn21.data.DataAccess;
import com.cn21.data.cache.Cache;
import com.cn21.data.cache.RealApiCacheFactory.RKey;
import com.cn21.data.cache.RealApiCacheFactory.RValue;
import com.cn21.module.Blacklist;

public class DataManager implements DataAccess {
	private ApiLimitedAdmin apiLimitedAdmin;
	private BlacklistAdmin blacklistAdmin;
	private RealApiAdmin realApiAdmin;
	
	private DataManager(){
		init();
	}
	
	public void init(){
		
	}

	public Blacklist getBlacklistByIp(String ip) {
		// TODO Auto-generated method stub
		return blacklistAdmin.getBlacklistByIp(ip);
	}

	public Blacklist getBlacklistByUsername(String username) {
		// TODO Auto-generated method stub
		return blacklistAdmin.getBlacklistByUsername(username);
	}

	public void setBlacklist(Blacklist blacklist) {
		// TODO Auto-generated method stub
		blacklistAdmin.setBlacklist(blacklist);
	}

	public void removeBlacklist(Blacklist blacklist) {
		// TODO Auto-generated method stub
		blacklistAdmin.removeBlacklist(blacklist);
	}

	public int getApiInterfaceId(String url, Map<String, String[]> parameters) {
		// TODO Auto-generated method stub
		return apiLimitedAdmin.getInterfaceId(url, parameters);
	}

	public int getLimitedTimesById(int interface_id) {
		// TODO Auto-generated method stub
		return apiLimitedAdmin.getLimitedTimes(interface_id);
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
