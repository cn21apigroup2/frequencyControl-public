package com.cn21.module;

public class RealTimes {
	private int interfaceId;
	private String identity;
	private int times;
	private long startTime;
	
	public RealTimes(int interfaceId, String identity, int times, long startTime) {
		this.interfaceId = interfaceId;
		this.identity = identity;
		this.times = times;
		this.startTime = startTime;
	}
	
	public int getInterfaceId() {
		return interfaceId;
	}
	public void setInterfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		return "RealTimes [interfaceId=" + interfaceId + ", identity=" + identity + ", times=" + times + ", startTime="
				+ startTime + "]";
	}
	
}
