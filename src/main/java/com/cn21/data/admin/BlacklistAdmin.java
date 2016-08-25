package com.cn21.data.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cn21.module.Blacklist;

/**
 * 黑名单数据管理
 * @author Administrator
 *
 */
public class BlacklistAdmin {
	//Map存储，加快查询速度
	private Map<String, Blacklist> blacklistMap;

	public BlacklistAdmin(List<Blacklist> lists){
		blacklistMap=new HashMap<String,Blacklist>();
		if(lists==null) return;
		for(int i=0;i<lists.size();++i){
			Blacklist blacklist=lists.get(i);
			String key=getKey(blacklist);
			blacklistMap.put(key, blacklist);
		}
	}
	
	/**
	 * 根据ip获取黑名单信息
	 * @param ip
	 * @return
	 */
	public Blacklist getBlacklistByIp(String ip){
		return getBlacklistByKey(ip);
	}
	
	/**
	 * 根据用户名获取黑名单
	 * @param username
	 * @return
	 */
	public Blacklist getBlacklistByUsername(String username){
		return getBlacklistByKey(username);
	}
	
	private Blacklist getBlacklistByKey(String key){
		return blacklistMap.get(key);
	}
	
	/**
	 * 设置黑名单数据
	 * @param blacklist
	 * @return
	 */
	public synchronized void setBlacklist(Blacklist blacklist){
		String key=getKey(blacklist);
		blacklistMap.put(key, blacklist);
		//update
		
	}
	
	/**
	 * 删除黑名单数据
	 * @param blacklist
	 */
	public void removeBlacklist(Blacklist blacklist){
		Blacklist b=blacklistMap.remove(getKey(blacklist));
		//update
		
	}
	
	/**
	 * 获取blacklist对应的键
	 * @param blacklist
	 * @return
	 */
	public String getKey(Blacklist blacklist){
		if(blacklist.getUsername()!=null) return blacklist.getUsername();
		if(blacklist.getLimitedIp()!=null)  return blacklist.getLimitedIp();
		return "";
	}
}
