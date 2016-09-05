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
		if(Integer.parseInt(request.getAttribute(ACCESSTOKEN).toString())==CONTINUE){
			System.out.println("验证黑名单");
			if(accessInfo.getUsername()==null||accessInfo.getUsername().equals("")){
				System.out.println("通过IP验证黑名单");
				Blacklist blacklist=dataManager.getBlacklistByIp(accessInfo.getIpAddress());
				if(blacklist!=null){
					if(!getToken(blacklist)){
						request.setAttribute(ACCESSTOKEN, REFUSED);
						request.setAttribute("errorMsg","被加入黑名单，拒绝访问");
					}
				}
			}else{
				System.out.println("通过用户名验证黑名单");
				Blacklist blacklist=dataManager.getBlacklistByUsername(accessInfo.getUsername());
				if(blacklist!=null){
					if(!getToken(blacklist)){
						request.setAttribute(ACCESSTOKEN, REFUSED);
					}
				}
			}
			System.out.println("黑名单验证结束");
		}

	}

	@Override
	public void intercept(Object... objects) {
		// TODO Auto-generated method stub
		
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
