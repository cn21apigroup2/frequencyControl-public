package com.cn21.web.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.naming.NoInitialContextException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.LifecycleException;

import com.cn21.data.admin.DataManager;
import com.cn21.data.admin.RealApiAdmin.RealApiConfig;
import com.cn21.web.dto.AccessInfo;
import com.cn21.web.interceptor.Interceptor;
import com.cn21.web.interceptor.config.OverallSituationToken;
import com.cn21.web.interceptor.proxy.InterceptorProxy;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Servlet Filter implementation class RequestDispatchFilter
 */
//@WebFilter("/*")
public class RequestDispatchFilter implements Filter {

	private String TransferParamClassName;
	
	private int initCapicity;
	
	private String appConfig;
	
	private String appKey;
	
	private String appSecret;

    /**
     * Default constructor. 
     */
    public RequestDispatchFilter() {

    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		DataManager.getInstance().onEnd();
	}

	/**
	 *
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp =(HttpServletResponse)response;
		req.setAttribute("errorMsg",null);
		req.setAttribute("accessToken",0);
		AccessInfo accessInfo=getAccessInfo(req);
		System.out.println(accessInfo==null);
		if(accessInfo==null){
			accessInfo = new AccessInfo();
		}
		if(accessInfo.getIpAddress()==null){
			accessInfo.setIpAddress(getRequestIp(req));
		}
		Interceptor interceptor = new Interceptor();
		InterceptorProxy interceptorProxy = new InterceptorProxy();
		Interceptor agentClass = (Interceptor)interceptorProxy.createProxy(interceptor);
		agentClass.intercept(req,resp,accessInfo);
		if(Integer.parseInt(req.getAttribute("accessToken").toString())!=-1){
			chain.doFilter(request, response);
		}else{
			if(request.getAttribute("errorMsg")!=null){
				System.out.println(request.getAttribute("errorMsg"));
			}
			//req.getRequestDispatcher("/error.flt").forward(request, response);
		}

	}

	/**
	 *
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("容器启动开始");
		readConfig(fConfig);
		RealApiConfig config = new RealApiConfig(initCapicity);
		try {
			DataManager.getInstance().init(appKey, appSecret, config);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1){
			e1.printStackTrace();
		}
		int times =DataManager.getInstance().getGlobalLimited().getTimeoutOfSeconds();
		int frequency=DataManager.getInstance().getGlobalLimited().getFrequency();
		try {
			if(frequency>0){
				OverallSituationToken.getInstance().createLimiter(frequency/times);
			}else{
				OverallSituationToken.getInstance().createLimiter(Integer.MAX_VALUE);
			}
		} catch (LifecycleException e) {
			e.printStackTrace();
		}
		System.out.println("容器启动完成");
	}
	
	/**
	 *
	 * @return
	 */
	private AccessInfo getAccessInfo(HttpServletRequest request){
		if(TransferParamClassName==null){
			try {
				throw new NoInitialContextException("need to configure the param in RequestDispatchFilter of TransferParamClasss");
			} catch (NoInitialContextException e) {
				e.printStackTrace();
			}
		}
		
		try {
			Class clazz = Class.forName(TransferParamClassName);
			Method method = clazz.getMethod("getAccessInfo",HttpServletRequest.class);
			return (AccessInfo) method.invoke(clazz.newInstance(),request);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	private void readConfig(FilterConfig fConfig){
		Properties properties = new Properties();
		String filePath=fConfig.getInitParameter("appConfig");
		String realPath=getClass().getResource("/").getPath()+"/"+filePath;
		if(filePath!=null){
			FileInputStream in =null;
			try {
				in= new FileInputStream(new File(realPath));
				properties.load(in);
				this.TransferParamClassName=properties.getProperty("TransferParamClassName");
				this.initCapicity=Integer.parseInt(properties.getProperty("initCapicity"));
				this.appKey=properties.getProperty("appKey");
				this.appSecret=properties.getProperty("appSecret");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			this.TransferParamClassName=fConfig.getInitParameter("TransferParamClassName");
			this.initCapicity=Integer.parseInt(fConfig.getInitParameter("initCapicity"));
			this.appKey=fConfig.getInitParameter("appKey");
			this.appSecret=fConfig.getInitParameter("appSecret");		
		}
	}
	private String getRequestIp(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
}
