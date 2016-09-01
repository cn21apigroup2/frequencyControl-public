package com.cn21.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn21.web.dto.AccessInfo;

public interface HttpInterceptorHandler{

	void intercept(HttpServletRequest request, HttpServletResponse response, AccessInfo accessInfo);

}
