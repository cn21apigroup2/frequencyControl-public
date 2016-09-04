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
			if(accessInfo.getUsername()==null||accessInfo.getUsername().equals("")){
				Blacklist blacklist=dataManager.getBlacklistByIp(accessInfo.getIpAddress());
				if(blacklist!=null){
					if(!getToken(blacklist)){
						request.setAttribute(ACCESSTOKEN, REFUSED);
					}
				}
			}else{
				Blacklist blacklist=dataManager.getBlacklistByUsername(accessInfo.getUsername());
				if(blacklist!=null){
					if(!getToken(blacklist)){
						request.setAttribute(ACCESSTOKEN, REFUSED);
					}
				}
			}
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
