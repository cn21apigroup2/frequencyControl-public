package com.cn21.data.admin;

import java.sql.SQLException;

import com.cn21.data.cache.Cache;
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
	
	public RealApiAdmin() throws ClassNotFoundException{
		cache=RealApiCacheFactory.createCache("lru", 20000);
		db=new RealApiDb();
	}
	
	public int getCurrentTimesByUsername(int interface_id, String username) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCurrentTimesByIp(int interface_id, String ip) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private int getCurrentTimesByKey(int interface_id,String key) throws SQLException{
		int times=-1;
		RealTimes rt;
		RKey rkey=new RKey(key, interface_id);
		RValue rvalue=cache.get(rkey);
		if(rvalue==null){
			rt=db.getRealTimes(interface_id, key);
		}else  rt=new RealTimes(interface_id, key, rvalue.times, rvalue.startTimes);
		checkExpire(rt);
		return times;
	}

	private void checkExpire(RealTimes rt) {
		// TODO Auto-generated method stub
		
	}

	public int addTimesByUsername(int interface_id, String username) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int addTimesByIp(int interface_id, String ip) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
