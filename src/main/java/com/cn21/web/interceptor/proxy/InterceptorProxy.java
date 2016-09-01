package com.cn21.web.interceptor.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn21.web.dto.AccessInfo;
import com.cn21.web.util.InterceptorXMLParse;

/**
 * Created by steven on 2016/8/23.
 */

/**
 * 拦截器的动态代理，动态加载拦截器
 * @author steven
 *
 */
public class InterceptorProxy implements MethodInterceptor {
	
	/**
	 * 被代理目标
	 */
    private Object target;
    
    
    /**
     * 创建代理后的类
     * @param target
     * @return
     */
    public Object createProxy(Object target){
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        doBefore((HttpServletRequest)objects[0],(HttpServletResponse)objects[1],(AccessInfo)objects[2]);
        result = methodProxy.invokeSuper(o,objects);
        return result;
    }
    /**
     * 被代理方法前执行的方法
     * @param request
     * @param response
     * @param accessInfo
     */
    private void doBefore(HttpServletRequest request,HttpServletResponse response,AccessInfo accessInfo) {
    	InterceptorXMLParse interceptorXMLParse = new InterceptorXMLParse();
    	List<String> interceptors = interceptorXMLParse.parse("Interceptor.xml");
    	try{
	    	for(String s:interceptors){
	    		Class clazz = Class.forName(s);
	    		Method method=clazz.getMethod("intercept",HttpServletRequest.class,HttpServletResponse.class,AccessInfo.class);
	    		method.invoke(clazz.newInstance(),request,response,accessInfo);
	    	}
    	}catch(ClassNotFoundException e1){
    		e1.printStackTrace();
    	}catch(InstantiationException e2){
    		e2.printStackTrace();
    	}catch(IllegalAccessException e3){
    		e3.printStackTrace();
    	}catch(NoSuchMethodException e4){
    		e4.printStackTrace();
    	}catch(SecurityException e5){
    		e5.printStackTrace();
    	}catch(IllegalArgumentException e6){
    		e6.printStackTrace();
    	}catch(InvocationTargetException e7){
    		e7.printStackTrace();
    	}
    }
}