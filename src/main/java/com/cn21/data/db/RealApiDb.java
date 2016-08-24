package com.cn21.data.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cn21.module.RealTimes;

public class RealApiDb {
	private static final String driver="org.sqlite.JDBC";
	private static final String url="jdbc:sqlite:"+RealApiDb.class.getResource("")+"realapi.db";
	private static final String username="";
	private static final String password="";
	
	private DbUtil db;
	
	public RealApiDb() throws ClassNotFoundException{
		db=new DbUtil(driver,url,username,password);
	}
	
	public RealTimes getRealTimes(int interfaceId,String identity) throws SQLException{
		RealTimes real=null;
		ResultSet rs=db.query("select * from real_times where interface_id="+interfaceId+" and identity='"+identity+"'");
		if(rs.next()){
			real=new RealTimes(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getLong(4));
		}
		return real;
	}
	
	public int addRealTimes(RealTimes realTimes) throws SQLException{
		if(realTimes==null) return 0;
		return db.execute("insert into real_times values("+realTimes.getInterfaceId()+",'"+realTimes.getIdentity()+"',"
					+realTimes.getTimes()+","+realTimes.getStartTime()+")");
	}
	
	public int updateRealTimes(RealTimes realTimes) throws SQLException{
		if(realTimes==null) return 0;
		return db.execute("update real_times set times="+realTimes.getTimes()+",start_time="+realTimes.getStartTime()
						+" where interface_id="+realTimes.getInterfaceId()+" and identity='"+realTimes.getIdentity()+"'");
	}
	
}
