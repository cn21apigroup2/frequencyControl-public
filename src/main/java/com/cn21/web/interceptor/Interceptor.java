package com.cn21.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn21.web.dto.AccessInfo;


/**
 * 被代理拦截器，通过对该拦截器进行代理实现拦截器的动态加载
 * @author steven
 *
 */
public class Interceptor extends AbstractInterceptorHandler{
	
	@Override
	public void intercept(Object... objects) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void intercept(HttpServletRequest request, HttpServletResponse response, AccessInfo accessInfo) {
		// TODO Auto-generated method stub

	}


}
