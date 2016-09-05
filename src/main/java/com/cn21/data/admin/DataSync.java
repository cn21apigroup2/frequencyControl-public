package com.cn21.data.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.cn21.frequencyControl_public.util.HttpUtil;
import com.cn21.module.Blacklist;
import com.cn21.module.InterfaceControl;

/**
 * 数据同步，
 * @author Administrator
 *
 */
public class DataSync {
	private static Logger logger = LogManager.getLogger(DataSync.class);
	
	private static ScheduledExecutorService executor=Executors.newScheduledThreadPool(10);
	private ApiLimitedAdmin ad;
	private BlacklistAdmin bd;
	
	public DataSync(ApiLimitedAdmin ad,BlacklistAdmin bd){
		this.ad=ad;
		this.bd=bd;
		timer();
	}
	
	/**
	 * 定时任务
	 */
	private void timer(){
		//更新api频次数据，每2hours
		//executor.scheduleAtFixedRate(new PullApiLimited(), 2, 2, TimeUnit.HOURS);
		logger.info("启动定时：每1hour push 黑名单");
		executor.scheduleAtFixedRate(new PushBlacklist(), 1, 1, TimeUnit.HOURS);
	}
	
	/**
	 * 拉取api频次控制数据
	 */
	public void pullApiLimitedData(){
		logger.info("启动线程，拉取api频次数据");
		executor.schedule(new PullApiLimited(), 0, TimeUnit.SECONDS);
	}
	
	/**
	 * push本地黑名单到服务器
	 */
	public void pushBlacklists(){
		executor.schedule(new PushBlacklist(), 0, TimeUnit.SECONDS);
	}
	
	public void close(){
		try {
			executor.awaitTermination(2, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 拉取api频次控制数据线程
	 * @author Administrator
	 *
	 */
	class PullApiLimited implements Runnable{
		
		public void run() {
			//get
			try {
				Map<String,List<InterfaceControl>> map=HttpUtil.getFromServer(DataManager.getInstance().getAppKey());
				List<InterfaceControl> list=map.get("interfaces");
				ad.refreshData(list);
				List<InterfaceControl> gi=map.get("overallControl");
				if(gi!=null&&gi.size()>0)
					DataManager.getInstance().setGlobalInterface(gi.get(0));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * push本地黑名单到服务器的线程
	 * @author Administrator
	 *
	 */
	class PushBlacklist implements Runnable{

		public void run() {
			List<Blacklist> lists=bd.pushData();
			//update
			try {
				HttpUtil.updateBlackListToServer(DataManager.getInstance().getAppKey(),lists);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
