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
		if(Integer.parseInt(request.getAttribute(ACCESSTOKEN).toString())==CONTINUE){
			System.out.println("频次验证开始");
			int interfaceId = dataManager.getApiInterfaceId(request.getRequestURI(),request.getParameterMap());
			int times=0;
			String username=accessInfo.getUsername();
			String ip = accessInfo.getIpAddress();
			if(username==null||username.equals("")){
				System.out.println("通过IP验证频次");
				times=dataManager.getCurrentTimesByIp(interfaceId, ip);
				dataManager.addTimesByIp(interfaceId, ip);
			}else{
				times=dataManager.getCurrentTimesByUsername(interfaceId, username);
				dataManager.addTimesByUsername(interfaceId, username);
				System.out.println("通过用户名验证频次");
			}
			System.out.println("访问次数:"+times);
			if(times>dataManager.getLimitedTimesById(interfaceId)){
				updateBlacklist(username,ip);
				request.setAttribute("errorMsg","访问超频，拒绝访问");
				request.setAttribute(ACCESSTOKEN, REFUSED);
			}
			System.out.println("频次验证结束");
		}

	}

	@Override
	public void intercept(Object... objects) {
		// TODO Auto-generated method stub
		
	}
	
	private void updateBlacklist(String username,String ip){
		System.out.println("添加黑名单");
		Blacklist blacklist = null;
		if(username==null||username.equals("")){
			blacklist=DataManager.getInstance().getBlacklistByIp(ip);
		}else{
			blacklist=DataManager.getInstance().getBlacklistByUsername(username);
		}	
		if(blacklist==null){
			blacklist = new Blacklist();
			if(username!=null){
				blacklist.setUsername(username);
				blacklist.setLimitedIp(ip);
			}else{
				blacklist.setLimitedIp(ip);
			}
			blacklist.setTimes((short)1);
			blacklist.setFirDate(new Date());
			blacklist.setAppKey(DataManager.getInstance().getAppKey());
			DataManager.getInstance().addBlacklist(blacklist);
		}else{
			if(blacklist.getThrDate()!=null){
				blacklist.setAbsoluteDate(new Date());
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
