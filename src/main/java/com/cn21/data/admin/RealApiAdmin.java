package com.cn21.data.admin;

import com.cn21.data.cache.Cache;
import com.cn21.data.cache.RealApiCacheFactory.RKey;
import com.cn21.data.cache.RealApiCacheFactory.RValue;
import com.cn21.data.db.RealApiDb;

/**
 * 实时api频次的管理
 * @author Administrator
 *
 */
public class RealApiAdmin {
	private Cache<RKey,RValue> cache;
	private RealApiDb db;
	
	public RealApiAdmin(){
		
	}
}
