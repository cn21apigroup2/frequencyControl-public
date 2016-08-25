package com.cn21.frequencyControl_public;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.cn21.data.db.RealApiDb;
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
	
	@Test
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
		
	}
}
