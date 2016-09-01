package com.cn21.web.interceptor.config;

import java.util.concurrent.TimeUnit;

import org.apache.catalina.LifecycleException;

import com.google.common.util.concurrent.RateLimiter;
import com.sun.media.jfxmedia.events.NewFrameEvent;

public class OverallSituationToken {
	
	private static OverallSituationToken overallSituationToken=null;
	
	private RateLimiter limiter = null;
	
	
	public static OverallSituationToken getInstance(){
		if(overallSituationToken==null){
			overallSituationToken=new OverallSituationToken();
		}
		return overallSituationToken;
	}
	
	public void createLimiter(double permitsPerSecond) throws LifecycleException{
		if(limiter!=null){
			throw new LifecycleException("limiter allerady exits");
		}
		limiter=RateLimiter.create(permitsPerSecond);
	}
	
	public void createLimiter(double permitsPerSecond,long warmupPeriod,TimeUnit unit) throws LifecycleException{
		if(limiter!=null){
			throw new LifecycleException("limiter allerady exits");
		}
		limiter=RateLimiter.create(permitsPerSecond, warmupPeriod, unit);
	}
	
	public RateLimiter getLimiter() throws Exception{
		if(limiter==null){
			throw new Exception("limit not init exception");
		}
		return limiter;
	}
}
