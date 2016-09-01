package com.cn21.web.interceptor;

public abstract class AbstractInterceptorHandler implements HttpInterceptorHandler,InterceptorHandler {
	protected final int ALLOW = 1 ;
	protected final int REFUSED = -1;
	protected final int CONTINUE = 0; 
	protected final String ACCESSTOKEN="accessToken";
	
}
