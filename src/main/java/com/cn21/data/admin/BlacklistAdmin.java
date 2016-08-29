package com.cn21.data.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.cn21.module.Blacklist;

/**
 * 黑名单数据管理
 * @author Administrator
 *
 */
public class BlacklistAdmin {
	//Map存储，加快查询速度
	private Map<String, Blacklist> blacklistMap;
	private ReentrantReadWriteLock lock;
	private WriteLock wlock;
	private ReadLock rlock;

	public BlacklistAdmin(List<Blacklist> lists){
		blacklistMap=new ConcurrentHashMap<String,Blacklist>();
		lock=new ReentrantReadWriteLock();
		wlock=lock.writeLock();
		rlock=lock.readLock();
		init(lists);
	}
	
	public void init(List<Blacklist> lists){
		if(lists!=null){
			blacklistMap.clear();
			for(int i=0;i<lists.size();++i){
				Blacklist blacklist=lists.get(i);
				String key=getKey(blacklist);
				blacklistMap.put(key, blacklist);
			}
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
		rlock.lock();
		Blacklist b=blacklistMap.get(key);
		rlock.unlock();
		return b;
	}
	
	/**
	 * 设置黑名单数据
	 * @param blacklist
	 * @return
	 */
	public synchronized void setBlacklist(Blacklist blacklist){
		String key=getKey(blacklist);
		rlock.lock();
		Blacklist old=blacklistMap.put(key, blacklist);
		rlock.unlock();
		//update
		
	}
	
	/**
	 * 删除黑名单数据
	 * @param blacklist
	 */
	public void removeBlacklist(Blacklist blacklist){
		rlock.lock();
		Blacklist b=blacklistMap.remove(getKey(blacklist));
		rlock.unlock();
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
	
	public void refreshData(List<Blacklist> lists){
		wlock.lock();
		init(lists);
		wlock.unlock();
	}
	
	public List<Blacklist> pushData(){
		List<Blacklist> lists=new ArrayList<Blacklist>();
		wlock.lock();
		for(String key:blacklistMap.keySet()){
			lists.add(blacklistMap.get(key));
		}
		wlock.unlock();
		return lists;
	}
}
