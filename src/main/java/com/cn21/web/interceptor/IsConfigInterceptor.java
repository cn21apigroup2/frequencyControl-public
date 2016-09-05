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
			System.out.println("读取API配置开始");
			int interfaceId=0;
			interfaceId=DataManager.getInstance().getApiInterfaceId(request.getRequestURI(), request.getParameterMap());
			if(interfaceId<0){
				request.setAttribute(ACCESSTOKEN, ALLOW);
				System.out.println("验证通过");
			}
			System.out.println("读取API配置结束");
		}
	}

	@Override
	public void intercept(Object... objects) {
		// TODO Auto-generated method stub
		
	}

}
