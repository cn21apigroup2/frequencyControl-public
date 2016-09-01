package com.cn21.web.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn21.data.admin.DataManager;
import com.cn21.module.Blacklist;
import com.cn21.web.dto.AccessInfo;

public class BlacklistCheckInterceptor extends AbstractInterceptorHandler {

	@Override
	public void intercept(HttpServletRequest request, HttpServletResponse response, AccessInfo accessInfo) {
		DataManager dataManager=DataManager.getInstance();
		if(Integer.parseInt((String) request.getAttribute(ACCESSTOKEN))==CONTINUE){
			//username为空则通过ip判断
			if(accessInfo.getUsername()==null||accessInfo.getUsername().equals("")){
				//判断是否输入IP，否则通过REQUEST获取IP
				if(accessInfo.getIpAddress()==null||accessInfo.getIpAddress().equals("")){		
					String ip=getRequestIp(request);
					accessInfo.setIpAddress(ip);
					Blacklist blacklist=dataManager.getBlacklistByIp(ip);
					if(blacklist!=null){
						if(!getToken(blacklist))
							request.setAttribute(ACCESSTOKEN, REFUSED);
				    }
				}else{
					Blacklist blacklist=dataManager.getBlacklistByIp(accessInfo.getIpAddress());
					if(blacklist!=null){	
						if(!getToken(blacklist))
							request.setAttribute(ACCESSTOKEN, REFUSED);
					}
				}

			}else{
				if(dataManager.getBlacklistByUsername(accessInfo.getUsername())!=null){
					request.setAttribute(ACCESSTOKEN, REFUSED);
				}
			}
		}
		
	}

	@Override
	public void intercept(Object... objects) {
		// TODO Auto-generated method stub
		
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

	private boolean getToken(Blacklist blacklist){
		if(blacklist.getAbsoluteDate()!=null){
			return false;
		}
		if(blacklist.getThrDate()!=null){
			Date now = new Date();
			if((now.getTime()-blacklist.getThrDate().getTime())<(long)86400000){
				return false;
			}		
		}
		if(blacklist.getSecDate()!=null){
			Date now = new Date();
			if((now.getTime()-blacklist.getSecDate().getTime())<(long)86400000){
				return false;
			}		
		}
		if(blacklist.getFirDate()!=null){
			Date now = new Date();
			if((now.getTime()-blacklist.getFirDate().getTime())<(long)86400000){
				return false;
			}	
		}
		return true;
	}
}
