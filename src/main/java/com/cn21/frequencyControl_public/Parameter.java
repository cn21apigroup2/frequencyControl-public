/**
 *  @Title: InterfaceConTrole.java 
 *  @Package com.cn21.FrequencyControl.module 
 *  @Description: TODO(用一句话描述该文件做什么) 
 *  @author chenxiaofeng
 *  @date 2016年8月22日 下午2:28:46 
 *  @version V1.0 
 */
package com.cn21.frequencyControl_public;

/**
 * @author chenxiaofeng
 * @date 2016年8月22日
 */
public class Parameter {
	private long parameter_id;
	private String parameter_key;
	private String parameter_value;
	private long interface_id;
	/**
	 * @return the parameter_id
	 */
	public long getParameter_id() {
		return parameter_id;
	}
	/**
	 * @param parameter_id the parameter_id to set
	 */
	public void setParameter_id(long parameter_id) {
		this.parameter_id = parameter_id;
	}
	/**
	 * @return the parameter_key
	 */
	public String getParameter_key() {
		return parameter_key;
	}
	/**
	 * @param parameter_key the parameter_key to set
	 */
	public void setParameter_key(String parameter_key) {
		this.parameter_key = parameter_key;
	}
	/**
	 * @return the parameter_value
	 */
	public String getParameter_value() {
		return parameter_value;
	}
	/**
	 * @param parameter_value the parameter_value to set
	 */
	public void setParameter_value(String parameter_value) {
		this.parameter_value = parameter_value;
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

}
