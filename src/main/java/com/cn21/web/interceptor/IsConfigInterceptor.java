package com.cn21.web.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn21.data.admin.DataManager;
import com.cn21.web.dto.AccessInfo;



public class IsConfigInterceptor extends AbstractInterceptorHandler{

	@Override
	public void intercept(HttpServletRequest request, HttpServletResponse response, AccessInfo accessInfo) {
		// TODO Auto-generated method stub
		if(Integer.parseInt(request.getAttribute(ACCESSTOKEN).toString())==CONTINUE){
			int interfaceId=0;
			interfaceId=DataManager.getInstance().getApiInterfaceId(request.getRequestURI(), request.getParameterMap());
			if(interfaceId<0){
				request.setAttribute(ACCESSTOKEN, ALLOW);
			}
		}
	}

	@Override
	public void intercept(Object... objects) {
		// TODO Auto-generated method stub
		
	}

}
