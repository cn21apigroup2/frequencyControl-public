package com.cn21.web.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn21.data.admin.DataManager;
import com.cn21.module.Blacklist;
import com.cn21.web.dto.AccessInfo;

public class OverclockingInterceptor extends AbstractInterceptorHandler{

	@Override
	public void intercept(HttpServletRequest request, HttpServletResponse response, AccessInfo accessInfo) {
		DataManager dataManager = DataManager.getInstance();
		//是否需要拦截
		if(Integer.parseInt((String) request.getAttribute(ACCESSTOKEN))==CONTINUE){
			//用户名是否为空
			int interfaceId = dataManager.getApiInterfaceId(request.getRequestURI(),request.getParameterMap());
			int times=0;
			String username=accessInfo.getUsername();
			String ip = accessInfo.getIpAddress();
			if(username==null||username.equals("")){
				times=dataManager.getCurrentTimesByIp(interfaceId, ip);
			}else{
				times=dataManager.getCurrentTimesByUsername(interfaceId, username);
			}
			if(times>dataManager.getLimitedTimesById(interfaceId)){
				updateBlacklist(username,ip);
				request.setAttribute(ACCESSTOKEN, REFUSED);
			}
		}
	}

	@Override
	public void intercept(Object... objects) {
		// TODO Auto-generated method stub
		
	}
	
	private void updateBlacklist(String username,String ip){
		Blacklist blacklist=null;
		if(username!=null||!username.equals("")){
			blacklist=DataManager.getInstance().getBlacklistByUsername(username);
		}else{
			blacklist=DataManager.getInstance().getBlacklistByIp(ip);
		}	
		if(blacklist==null){
			blacklist = new Blacklist();
			if(username!=null){
				blacklist.setUsername(username);
			}else{
				blacklist.setLimitedIp(ip);
			}
			blacklist.setTimes((short)1);
			blacklist.setFirDate(new Date());
			DataManager.getInstance().addBlacklist(blacklist);
		}else{
			if(blacklist.getThrDate()!=null){
				blacklist.setAbsoulteDate(new Date());
			}else if(blacklist.getSecDate()!=null){
				blacklist.setThrDate(new Date());
			}else if(blacklist.getFirDate()!=null){
				blacklist.setSecDate(new Date());
			}else{
				blacklist.setFirDate(new Date());
			}
			short times = blacklist.getTimes();
			blacklist.setTimes((short) (times+1));
			DataManager.getInstance().setBlacklist(blacklist);
		}
	}

}
