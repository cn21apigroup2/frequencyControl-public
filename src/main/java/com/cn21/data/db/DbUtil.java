package com.cn21.data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.BasicDataSource;

public class DbUtil {
	private String driverName;
	private String url;
	private String username;
	private String password;
	private BasicDataSource ds;
	
	public DbUtil(String driverName, String url, String username, String password) throws ClassNotFoundException{
		this(driverName,url,username,password,null);
	}
	
	public DbUtil(String driverName, String url, String username, String password,BasicDataSource ds) throws ClassNotFoundException {
		this.driverName = driverName;
		this.url = url;
		this.username = username;
		this.password = password;
		if(ds==null){
			ds=new BasicDataSource();
			ds.setDriverClassName(driverName);
			ds.setUrl(url);
			ds.setInitialSize(10);
			ds.setMaxActive(20);
			ds.setMinIdle(5);
		}
		this.ds=ds;
		Class.forName(driverName);
	}
	
	public Connection getConnection() throws SQLException{
		Connection connection=null;
		if(ds!=null){
			connection=ds.getConnection();
		}
		else connection=DriverManager.getConnection(url,username,password);
		return connection;
	}
	
	public ResultSet query(String sql) throws SQLException{
		Connection conn=getConnection();
		Statement statement=conn.createStatement();
		ResultSet rs=statement.executeQuery(sql);
		return rs;
	}
	
	public int execute(String sql) throws SQLException{
		Connection conn=getConnection();
		Statement statement=conn.createStatement();
		return statement.executeUpdate(sql);
	}
	
	public String getDriverName() {
		return driverName;
	}
	
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
