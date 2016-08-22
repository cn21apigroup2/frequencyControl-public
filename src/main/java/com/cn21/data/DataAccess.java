package com.cn21.data;

import java.util.Map;

import com.cn21.module.Blacklist;

/**
 * 数据访问，控制接口
 * @author Administrator
 *
 */
public interface DataAccess {

	/**
	 * 根据ip获取黑名单信息
	 * @param ip
	 * @return
	 */
	public Blacklist getBlacklistByIp(String ip);
	
	/**
	 * 根据用户名获取黑名单
	 * @param username
	 * @return
	 */
	public Blacklist getBlacklistByUsername(String username);
	
	/**
	 * 设置黑名单数据
	 * @param blacklist
	 * @return
	 */
	public void setBlacklist(Blacklist blacklist);
	
	/**
	 * 删除黑名单数据
	 * @param blacklist
	 */
	public void removeBlacklist(Blacklist blacklist);
	
	/**
	 * 根据链接和参数得到
	 * @param url 链接
	 * @param parameters
	 * @return interface_id return -1 if not exist
	 */
	public int getApiInterfaceId(String url,Map<String,String> parameters);
	
	/**
	 * 根据interface_id得到限制次数
	 * @param interface_id
	 * @return
	 */
	public int getLimitedTimesById(int interface_id);
	
	/**
	 * 根据username得到当前次数
	 * @param interface_id
	 * @param username
	 * @return
	 */
	public int getCurrentTimesByUsername(int interface_id,String username);
	
	/**
	 * 根据ip得到当前次数
	 * @param interface_id
	 * @param ip
	 * @return
	 */
	public int getCurrentTimesByIp(int interface_id,String ip);
	
	/**
	 * 次数加1
	 * @param interface_id
	 * @param username
	 * @return
	 */
	public int addTimesByUsername(int interface_id,String username);
	
	/**
	 * 次数加1
	 * @param interface_id
	 * @param ip
	 * @return
	 */
	public int addTimesByIp(int interface_id,String ip);
	
}
