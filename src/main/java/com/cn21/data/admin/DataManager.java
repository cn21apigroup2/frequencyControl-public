package com.cn21.data.admin;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.cn21.data.DataAccess;
import com.cn21.data.admin.RealApiAdmin.RealApiConfig;
import com.cn21.data.socket.Client;
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
	private static Logger logger = LogManager.getLogger(DataManager.class);
	static{
		Properties pro=new Properties();
		pro.put("log4j.rootLogger", "INFO,stdout");
		pro.put("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
		pro.put("log4j.appender.stdout.Target", "System.out");
		pro.put("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
		pro.put("log4j.appender.stdout.layout.ConversionPattern", "[%-5p] %d{yyyy-MM-dd HH:mm:ss,SSS} method:%l%n%m%n");
		PropertyConfigurator.configure(pro);
	}
	
	private ApiLimitedAdmin apiLimitedAdmin;
	private BlacklistAdmin blacklistAdmin;
	private RealApiAdmin realApiAdmin;
	private DataSync dataSync;
	private InterfaceControl globalInterface;
	private String appKey;
	private String appSecret;
	private Client client;
	
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
		logger.info("初始化数据...");
		if(appKey==null||appSecret==null) throw new IllegalArgumentException("appKey and appSecret cannot be null");
		this.appKey=appKey;
		this.appSecret=appSecret;
		logger.info("从服务器中获取数据。。。");
		Map<String,List<InterfaceControl>> map=HttpUtil.getFromServer(appKey);
		//Map<String,List<InterfaceControl>> map=HttpUtil.getInterfacesTest();
		apiLimitedAdmin=new ApiLimitedAdmin(map.get("interfaces"));
		List<InterfaceControl> gi=map.get("overallControl");
		if(gi!=null&&gi.size()>0)
			this.globalInterface=gi.get(0);
		else this.globalInterface=null;
		List<Blacklist> blacklists=HttpUtil.getBlackListFromServer(appKey);
		blacklistAdmin=new BlacklistAdmin(blacklists);
		//blacklistAdmin=new BlacklistAdmin(HttpUtil.getBlacklistsTest());
		logger.info("从服务器中获取数据完成");
		//setBlacklistNextId(blacklists);
		realApiAdmin=new RealApiAdmin(apiLimitedAdmin,config);
		dataSync=new DataSync(apiLimitedAdmin, blacklistAdmin);
		try{
			client=new Client(appKey,appSecret, dataSync);
			client.start();
		}catch(Exception e){
			System.out.println("connect server socket fail");
		}
		logger.info("初始化datamanager成功");
	}

	private void setBlacklistNextId(List<Blacklist> lists){
		if(lists==null) return ;
		int max=0;
		for(int i=0;i<lists.size();++i){
			int id=lists.get(i).getBlacklistId();
			if(id>max)
				max=id;
		}
		Blacklist.NEXTID=max+1;
	}

	private void initLog4j() {
		InputStream filePath=DataManager.class.getResourceAsStream("/log4j.properties");
		PropertyConfigurator.configure(filePath);
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
			logger.error("get times error");
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
			logger.error("get times error");
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
		logger.info("onEnd: save and release...");
		if(dataSync!=null) dataSync.pushBlacklists();
		if(realApiAdmin!=null) realApiAdmin.flushToDb();
		if(realApiAdmin!=null) realApiAdmin.close();
		if(dataSync!=null) dataSync.close();
		if(client!=null) client.close();
		logger.info("onEnd: save and release completed...");
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
