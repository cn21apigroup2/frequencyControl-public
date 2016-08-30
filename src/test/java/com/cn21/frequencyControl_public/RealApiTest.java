package com.cn21.frequencyControl_public;

import java.sql.SQLException;
import com.cn21.data.cache.RealApiCacheFactory.RKey;
import com.cn21.data.cache.RealApiCacheFactory.RValue;

import org.junit.Before;
import org.junit.Test;

import com.cn21.data.db.RealApiDb;
import com.cn21.frequencyControl_public.util.sizeof.ClassIntrospector;
import com.cn21.frequencyControl_public.util.sizeof.ObjectInfo;
import com.cn21.module.RealTimes;

public class RealApiTest {
	private RealApiDb db;
	private RealTimes rt;
	
	@Before
	public void init(){
		try {
			db=new RealApiDb();
			rt=new RealTimes(1,"xiaoqinzhe",1,System.currentTimeMillis());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test
	public void controlDb(){
		try {
			db.addRealTimes(rt);
			rt.setTimes(2);
			db.updateRealTimes(rt);
			System.out.println(db.getRealTimes(1, "xiaoqinzhe").toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ClassIntrospector ci=new ClassIntrospector();
		try {
			ObjectInfo info=ci.introspect(new RKey("fa55555555555555555555555555sdkjhf",1));
			System.out.println(info.getDeepSize());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
