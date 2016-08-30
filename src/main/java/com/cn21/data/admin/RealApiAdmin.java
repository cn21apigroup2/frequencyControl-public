package com.cn21.data.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cn21.data.cache.Cache;
import com.cn21.data.cache.Cache.CacheListener;
import com.cn21.data.cache.RealApiCacheFactory;
import com.cn21.data.cache.RealApiCacheFactory.RKey;
import com.cn21.data.cache.RealApiCacheFactory.RValue;
import com.cn21.data.db.RealApiDb;
import com.cn21.module.RealTimes;

/**
 * 实时api频次的管理
 * @author Administrator
 *
 */
public class RealApiAdmin {
	private Cache<RKey,RValue> cache;
	private RealApiDb db;
	private ApiLimitedAdmin apiLimitedAdmin;
	private RealApiConfig config;
	
	public RealApiAdmin(ApiLimitedAdmin apiLimitedAdmin,RealApiConfig config) throws ClassNotFoundException{
		if(config==null) this.config=new RealApiConfig();
		else  this.config=config;
		cache=RealApiCacheFactory.createCache("lru", this.config.maxSize);
		cache.setListener(listener);
		db=new RealApiDb();
		this.apiLimitedAdmin=apiLimitedAdmin;
	}
	
	public int getCurrentTimesByUsername(int interface_id, String username) throws SQLException {
		// TODO Auto-generated method stub
		return getCurrentTimesByKey(interface_id,username);
	}

	public int getCurrentTimesByIp(int interface_id, String ip) throws SQLException {
		// TODO Auto-generated method stub
		return getCurrentTimesByKey(interface_id,ip);
	}
	
	private int getCurrentTimesByKey(int interface_id,String key) throws SQLException{
		int times=0;
		RealTimes rt=getRealTimes(interface_id,key);
		//System.err.println(rt.getTimes());
		checkExpire(rt);
		if(rt!=null) times=rt.getTimes();
		//System.err.println(rt.getTimes());
		return times;
	}

	private boolean checkExpire(RealTimes rt) {
		// TODO Auto-generated method stub
		if(rt==null) return false;
		int limitedTime=apiLimitedAdmin.getTimeout(rt.getInterfaceId());
		if(limitedTime>0){
			long now=System.currentTimeMillis()/1000;
			//System.out.println(rt.getStartTime()+" "+limitedTime+" "+now+" "+System.currentTimeMillis());
			if((rt.getStartTime()+limitedTime)<now){
				rt.setStartTime(now);
				rt.setTimes(0);				
				System.out.println("here");
				return true;
			}
		}
		return false;
	}

	public int addTimesByUsername(int interface_id, String username) {
		// TODO Auto-generated method stub
		return addTimes(interface_id,username);
	}

	public int addTimesByIp(int interface_id, String ip) {
		// TODO Auto-generated method stub
		return addTimes(interface_id,ip);
	}
	
	private int addTimes(int interface_id,String key){
		RKey rk=new RKey(key, interface_id);
		RValue rv=cache.get(rk);
		if(rv!=null){
			rv.times+=1;
			cache.set(rk, rv);
			return rv.times;
		}
		return -1;
	}
	
	private RealTimes getRealTimes(int interface_id,String key){
		RealTimes rt=null;
		RKey rk=new RKey(key, interface_id);
		RValue rv=cache.get(rk);
		if(rv==null){
			//listen error
			System.err.println("Rvalue is null in RealApiAdmin.getRealTimes");
		}else{
			rt=new RealTimes(rk.interfaceId,rk.identity,rv.times,rv.startTimes);
		}
		return rt;
	}
	
	private void setRealTimes(RealTimes rt){
		if(rt==null)  return ;
		RKey rk=new RKey(rt.getIdentity(),rt.getInterfaceId());
		RValue rv=new RValue(rt.getTimes(),rt.getStartTime());
		cache.set(rk, rv);
	}
	
	private void updateToDb(RealTimes rt){
		try {
			db.updateRealTimes(rt);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 将cache中的数据写进db
	 */
	public void flushToDb(){
		Map<RKey,RValue> map=cache.getData();
		if(map!=null){
			List<RealTimes> lists=new ArrayList<RealTimes>();
			for(RKey key:map.keySet()){
				RValue value=map.get(key);
				lists.add(getRealTimes(key,value));
			}
			try {
				db.updataBatch(lists);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private RealTimes getRealTimes(RKey key, RValue value) {
		// TODO Auto-generated method stub
		if(key!=null&&value!=null)
			return new RealTimes(key.interfaceId,key.identity,value.times,value.startTimes);
		return null;
	}

	public static class RealApiConfig{
		int maxSize=20000000;
		
		public RealApiConfig(){
			
		}
		
		public RealApiConfig(int maxSize){
			this.maxSize=maxSize;
		}

		public int getMaxSize() {
			return maxSize;
		}

		public void setMaxSize(int maxSize) {
			this.maxSize = maxSize;
		}
		
	}
	
	private CacheListener<RKey, RValue> listener=new CacheListener<RKey, RValue>() {

		public void onEntryRemove(RKey key, RValue oldValue, RValue newValue) {
			// TODO Auto-generated method stub
			
		}

		public RValue onEntryEvicted(RKey key, RValue oldValue, RValue newValue) {
			// TODO Auto-generated method stub
			RealTimes rt=new RealTimes(key.interfaceId, key.identity, newValue.times, newValue.startTimes);
			updateToDb(rt);
			return null;
		}

		public RValue onEntryCreate(RKey key) {
			// TODO Auto-generated method stub
			RValue rv=null;
			try {
				RealTimes rt=db.getRealTimes(key.interfaceId, key.identity);
				if(rt==null){
					rv=new RValue(0,System.currentTimeMillis()/1000);
					db.addRealTimes(new RealTimes(key.interfaceId,key.identity,rv.times,rv.startTimes));
				}else{
					rv=new RValue(rt.getTimes(),rt.getStartTime());
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return rv;
		}
	};
	
}
