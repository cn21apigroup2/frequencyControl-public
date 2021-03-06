/**
 *  @Title: InterfaceConTrole.java 
 *  @Package com.cn21.FrequencyControl.module 
 *  @Description: TODO(用一句话描述该文件做什么) 
 *  @author chenxiaofeng
 *  @date 2016年8月22日 下午2:28:46 
 *  @version V1.0 
 */
package com.cn21.module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author chenxiaofeng
 * @date 2016年8月22日
 */
public class InterfaceControl {
	private long interface_id;
	private long app_id;
	private String api_name;
	private int frequency;
	private int timeout;
	private char unit;
	private int can_loan;
	private int quota;
	private int deleted;
	private List<Parameter> parameters;
	private int timeoutTemp=-1;
	
	public int getTimeoutOfSeconds(){
		if(timeoutTemp!=-1)
			return timeoutTemp;
		int time=0;
		switch(unit){
		case 's':
			time=timeout;
			break;
		case 'm':
			time=timeout*60;
			break;
		case 'h':
			time=timeout*60*60;
			break;
		case 'd':
			time=timeout*24*60*60;
			break;
		}
		timeoutTemp=time;
		return timeoutTemp;
	}
	
	/**
	 * 把jsonArray字符串转化出pojo
	 * @param message
	 * @return
	 */
	public static InterfaceControl parseOverall(String message) {
		InterfaceControl result=null;
		JSONObject json = (JSONObject) JSONObject.parse(message);
		ObjectMapper objectMapper = new ObjectMapper();
			try {
				result=objectMapper.readValue(json.getString("overallControl"), InterfaceControl.class);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return result;
	}
	
	/**
	 * 把jsonArray字符串转化出pojo
	 * @param message
	 * @return
	 */
	public static List<InterfaceControl> parseCommon(String message) {
		List<InterfaceControl> result=new ArrayList<InterfaceControl>();
		JSONObject json = (JSONObject) JSONObject.parse(message);
		JSONArray parseArray = JSONArray.parseArray(json.getString("interfaces"));
		ObjectMapper objectMapper = new ObjectMapper();
		for(int i=0;i<parseArray.size();i++){
			try {
				result.add(objectMapper.readValue(parseArray.getString(i), InterfaceControl.class));
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 把jsonArray字符串转化出pojo
	 * @param message
	 * @return
	 */
	public static List<InterfaceControl> parse(String message) {
		List<InterfaceControl> result=new ArrayList<InterfaceControl>();
		JSONArray parseArray = JSONArray.parseArray(message);
		ObjectMapper objectMapper = new ObjectMapper();
		for(int i=0;i<parseArray.size();i++){
			try {
				result.add(objectMapper.readValue(parseArray.getString(i), InterfaceControl.class));
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * @return the paremeters
	 */
	public List<Parameter> getParameters() {
		return parameters;
	}
	/**
	 * @param paremeters the paremeters to set
	 */
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	/**
	 * @return the interface_id
	 */
	public long getInterface_id() {
		return interface_id;
	}
	/**
	 * @param interface_id the interface_id to set
	 */
	public void setInterface_id(long interface_id) {
		this.interface_id = interface_id;
	}
	/**
	 * @return the app_id
	 */
	public long getApp_id() {
		return app_id;
	}
	/**
	 * @param app_id the app_id to set
	 */
	public void setApp_id(long app_id) {
		this.app_id = app_id;
	}
	/**
	 * @return the api_name
	 */
	public String getApi_name() {
		return api_name;
	}
	/**
	 * @param api_name the api_name to set
	 */
	public void setApi_name(String api_name) {
		this.api_name = api_name;
	}
	/**
	 * @return the frequency
	 */
	public int getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}
	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	/**
	 * @return the unit
	 */
	public char getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(char unit) {
		this.unit = unit;
	}
	/**
	 * @return the can_loan
	 */
	public int getCan_loan() {
		return can_loan;
	}
	/**
	 * @param can_loan the can_loan to set
	 */
	public void setCan_loan(int can_loan) {
		this.can_loan = can_loan;
	}
	/**
	 * @return the quota
	 */
	public int getQuota() {
		return quota;
	}
	/**
	 * @param quota the quota to set
	 */
	public void setQuota(int quota) {
		this.quota = quota;
	}
	/**
	 * @return the deleted
	 */
	public int getDeleted() {
		return deleted;
	}
	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
}
