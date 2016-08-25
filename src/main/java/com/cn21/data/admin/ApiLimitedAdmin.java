package com.cn21.data.admin;

import java.util.List;
import java.util.Map;

import com.cn21.module.InterfaceControl;
import com.cn21.module.Parameter;

/**
 * api 频次表限制 管理
 * @author Administrator
 *
 */
public class ApiLimitedAdmin {

	private List<InterfaceControl> apis;
	
	public ApiLimitedAdmin(List<InterfaceControl> apis){
		this.apis=apis;
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
		return id;
	}
	
	public int getLimitedTimes(int interfaceId){
		int times=-1;
		for(int i=0;i<apis.size();++i){
			InterfaceControl item=apis.get(i);
			if(item.getInterface_id()==interfaceId)
				return item.getFrequency();
		}
		return times;
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

	public List<InterfaceControl> getApis() {
		return apis;
	}

	public synchronized void setApis(List<InterfaceControl> apis) {
		this.apis = apis;
	}
	
	
}
