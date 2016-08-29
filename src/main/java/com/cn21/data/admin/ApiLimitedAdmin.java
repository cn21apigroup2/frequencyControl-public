package com.cn21.data.admin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.cn21.module.InterfaceControl;
import com.cn21.module.Parameter;

/**
 * api 频次表限制 管理
 * @author Administrator
 *
 */
public class ApiLimitedAdmin {

	private List<InterfaceControl> apis;
	private ReentrantReadWriteLock lock;
	private WriteLock wlock;
	private ReadLock rlock;
	
	public ApiLimitedAdmin(List<InterfaceControl> apis){
		this.apis=apis;
		lock=new ReentrantReadWriteLock();
		wlock=lock.writeLock();
		rlock=lock.readLock();
	}
	
	/**
	 * 根据URL，参数获取iterface_id
	 * @param url
	 * @param params
	 * @return
	 */
	public int getInterfaceId(String url,Map<String,String[]> params){
		int id=-1;
		int count=-1;
		int times=0;
		rlock.lock();
		for(int i=0;i<apis.size();++i){
			InterfaceControl item=apis.get(i);
			if(url.equals(item.getApi_name())){
				int ccount=matchCount(item.getParameters(),params);
				if(ccount>count||(ccount==count&&item.getFrequency()<times)){
					id=(int) item.getInterface_id();
					count=ccount;
					times=item.getFrequency();
				}
			}
		}
		rlock.unlock();
		return id;
	}
	
	/**
	 * 获取次数
	 * @param interfaceId
	 * @return
	 */
	public int getLimitedTimes(int interfaceId){
		int times=-1;
		rlock.lock();
		for(int i=0;i<apis.size();++i){
			InterfaceControl item=apis.get(i);
			if(item.getInterface_id()==interfaceId)
				return item.getFrequency();
		}
		rlock.unlock();
		return times;
	}
	
	/**
	 * 获取时限
	 * @param interfaceId
	 * @return
	 */
	public int getTimeout(int interfaceId){
		int time=-1;
		rlock.lock();
		for(int i=0;i<apis.size();++i){
			InterfaceControl item=apis.get(i);
			if(item.getInterface_id()==interfaceId)
				return item.getTimeoutOfSeconds();
		}
		rlock.unlock();
		return time;
	}

	/**
	 * 判断 参数 是否满足限制中规定的 键值对
	 * @param parameters
	 * @param params
	 * @return
	 */
	private int matchCount(List<Parameter> parameters, Map<String, String[]> params) {
		if(parameters==null||params==null) return 0;
		int count=0;
		for(int i=0;i<parameters.size();++i){
			Parameter item=parameters.get(i);
			if(params.containsKey(item.getParameter_key())
					&&params.get(item.getParameter_key()).equals(item.getParameter_value())){
				++count;
			}
		}
		return count;
	}
	
	public void refreshData(List<InterfaceControl> newList){
		try {
			wlock.tryLock(120, TimeUnit.SECONDS);
			apis=newList;
			wlock.unlock();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<InterfaceControl> getApis() {
		return apis;
	}

	public void setApis(List<InterfaceControl> apis) {
		refreshData(apis);
	}
	
	
}
