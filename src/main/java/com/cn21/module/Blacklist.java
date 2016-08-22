package com.cn21.module;

import java.util.Date;

/**
 * 黑名单数据
 * @author Administrator
 *
 */
public class Blacklist {
	private int blacklistId;
	private String username;
	private String limitedIp;
	private short times;
	private Date firDate;
	private Date secDate;
	private Date thrDate;
	private Date absoulteDate;

	public int getBlacklistId() {
		return blacklistId;
	}

	public void setBlacklistId(int blacklistId) {
		this.blacklistId = blacklistId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLimitedIp() {
		return limitedIp;
	}

	public void setLimitedIp(String limitedIp) {
		this.limitedIp = limitedIp;
	}

	public short getTimes() {
		return times;
	}

	public void setTimes(short times) {
		this.times = times;
	}

	public Date getFirDate() {
		return firDate;
	}

	public void setFirDate(Date firDate) {
		this.firDate = firDate;
	}

	public Date getSecDate() {
		return secDate;
	}

	public void setSecDate(Date secDate) {
		this.secDate = secDate;
	}

	public Date getThrDate() {
		return thrDate;
	}

	public void setThrDate(Date thrDate) {
		this.thrDate = thrDate;
	}

	public Date getAbsoulteDate() {
		return absoulteDate;
	}

	public void setAbsoulteDate(Date absoulteDate) {
		this.absoulteDate = absoulteDate;
	}

}
