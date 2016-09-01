package com.cn21.web.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn21.data.admin.DataManager;
import com.cn21.web.dto.AccessInfo;
import com.cn21.web.interceptor.config.OverallSituationToken;
import com.google.common.util.concurrent.RateLimiter;

public class OverallSituationLimiteInterceptor extends AbstractInterceptorHandler{
	
	
	@Override
	public void intercept(HttpServletRequest request, HttpServletResponse response, AccessInfo accessInfo) {
		RateLimiter limiter=null;
		try {
			limiter=OverallSituationToken.getInstance().getLimiter();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!limiter.tryAcquire()){
			request.setAttribute(ACCESSTOKEN,REFUSED);
			request.setAttribute("errorMsg","服务器访问量过大,请稍后访问");
		}
	}

	@Override
	public void intercept(Object... objects) {
		// TODO Auto-generated method stub
		
	}

}
