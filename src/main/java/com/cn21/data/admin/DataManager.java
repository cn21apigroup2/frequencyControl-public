package com.cn21.data.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cn21.data.DataAccess;
import com.cn21.data.admin.RealApiAdmin.RealApiConfig;
import com.cn21.data.socket.ClientThread;
import com.cn21.frequencyControl_public.util.HttpUtil;
import com.cn21.module.Blacklist;
import com.cn21.module.InterfaceControl;

/**
 * 数据管理
 * @author Administrator
 *
 */
public class DataManager implements DataAccess {
	private ApiLimitedAdmin apiLimitedAdmin;
	private BlacklistAdmin blacklistAdmin;
	private RealApiAdmin realApiAdmin;
	private DataSync dataSync;
	private InterfaceControl globalInterface;
	private String appKey;
	private String appSecret;
	
	private DataManager(){
		//init();
	}
	
	/**
	 * 初始化配置
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void init(String appKey,String appSecret) throws IOException, ClassNotFoundException{
		init(appKey,appSecret,null);
	}
	
	/**
	 * 初始化配置
	 * @param config
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void init(String appKey,String appSecret,RealApiConfig config) throws IOException, ClassNotFoundException{
		if(appKey==null||appSecret==null) throw new IllegalArgumentException("appKey and appSecret cannot be null");
		this.appKey=appKey;
		this.appSecret=appSecret;
		Map<String,List<InterfaceControl>> map=HttpUtil.getFromServer(appKey);
		apiLimitedAdmin=new ApiLimitedAdmin(map.get("interfaces"));
		List<InterfaceControl> gi=map.get("overallControl");
		if(gi!=null&&gi.size()>0)
			this.globalInterface=gi.get(0);
		else this.globalInterface=null;
		//blacklistAdmin=new BlacklistAdmin(HttpUtil.getBlackListFromServer(appKey));
		blacklistAdmin=new BlacklistAdmin(HttpUtil.getBlacklistsTest());
		realApiAdmin=new RealApiAdmin(apiLimitedAdmin,config);
		dataSync=new DataSync(apiLimitedAdmin, blacklistAdmin);
		try{
			ClientThread client=new ClientThread(appKey,appSecret, dataSync);
			client.start();
		}catch(Exception e){
			System.out.println("connect server socket fail");
		}
		
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
	
	public void addBlacklist(Blacklist blacklist) {
		// TODO Auto-generated method stub
		blacklistAdmin.addBlacklist(blacklist);
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
	
	public InterfaceControl getGlobalLimited() {
		// TODO Auto-generated method stub
		return globalInterface;
	}

	public int getCurrentTimesByUsername(int interface_id, String username) {
		// TODO Auto-generated method stub
		try {
			return realApiAdmin.getCurrentTimesByUsername(interface_id, username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int getCurrentTimesByIp(int interface_id, String ip) {
		// TODO Auto-generated method stub
		try {
			return realApiAdmin.getCurrentTimesByIp(interface_id, ip);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int addTimesByUsername(int interface_id, String username) {
		// TODO Auto-generated method stub
		return realApiAdmin.addTimesByUsername(interface_id, username);
	}

	public int addTimesByIp(int interface_id, String ip) {
		// TODO Auto-generated method stub
		return realApiAdmin.addTimesByIp(interface_id, ip);
	}
	
	public static DataManager getInstance(){
		return inner.instance;
	}
	
	private static class inner{
		private static DataManager instance=new DataManager();
	}

	public void onEnd() {
		dataSync.pushBlacklists();
		realApiAdmin.flushToDb();
		dataSync.close();
	}
	
	public void setGlobalInterface(InterfaceControl interfaceControl){
		this.globalInterface=interfaceControl;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

}
