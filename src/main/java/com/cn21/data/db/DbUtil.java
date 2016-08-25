package com.cn21.data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtil {
	private String driverName;
	private String url;
	private String username;
	private String password;
	
	public DbUtil(String driverName, String url, String username, String password) throws ClassNotFoundException {
		this.driverName = driverName;
		this.url = url;
		this.username = username;
		this.password = password;
		Class.forName(driverName);
	}
	
	public Connection getConnection() throws SQLException{
		Connection connection=DriverManager.getConnection(url,username,password);
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
