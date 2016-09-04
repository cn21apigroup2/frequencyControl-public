/**
 *  @Title: Test.java 
 *  @Package com.cn21.FrequencyControl 
 *  @Description: TODO(用一句话描述该文件做什么) 
 *  @author chenxiaofeng
 *  @date 2016年8月8日 下午5:21:52 
 *  @version V1.0 
 */
package com.cn21.frequencyControl_public.util;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn21.module.Blacklist;
import com.cn21.module.InterfaceControl;

public class HttpUtil {

	// 读取超时
	private final static int SOCKET_TIMEOUT = 10000;
	// 连接超时
	private final static int CONNECTION_TIMEOUT = 10000;
	// 请求超时
	private final static int REQUEST_TIMEOUT = 10000;
	// 每个HOST的最大连接数量
	private final static int MAX_CONN_PRE_HOST = 20;
	// 连接池的最大连接数
	private final static int MAX_CONN = 60;
	// 连接池
	private static PoolingHttpClientConnectionManager connectionManager = null;
	//HttpClient建造器
	private static HttpClientBuilder httpBulder = null;
	//请求配置
	private static RequestConfig requestConfig = null;
	//请求地址
	private static String IP = "http://127.0.0.1";
	//端口号
	private static int PORT = 8080;

	static {
		// 设置http的状态参数
		requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT)
				.setConnectTimeout(CONNECTION_TIMEOUT)
				.setConnectionRequestTimeout(REQUEST_TIMEOUT).build();

		HttpHost target = new HttpHost(IP, PORT);
		connectionManager = new PoolingHttpClientConnectionManager();
		// 将最大连接数增加
		connectionManager.setMaxTotal(MAX_CONN);
		// 将每个路由基础的连接增加
		connectionManager.setDefaultMaxPerRoute(MAX_CONN_PRE_HOST);
		// 将目标主机的最大连接数增加
		connectionManager.setMaxPerRoute(new HttpRoute(target),
				MAX_CONN_PRE_HOST);
		httpBulder = HttpClients.custom();
		httpBulder.setConnectionManager(connectionManager);
	}

	/**
	 * 获取httpClient
	 * @return
	 */
	public static CloseableHttpClient getHttpClient() {
		CloseableHttpClient httpClient = httpBulder.build();
		return httpClient;
	}

	/**
	 * 构造请求方法post/get
	 * @param map
	 * @param url
	 * @param method
	 * @return
	 */
	public static HttpUriRequest getRequestMethod(Map<String, String> map,
			String url, String method) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : map.keySet()) {
			NameValuePair pair = new BasicNameValuePair(key, map.get(key));
			params.add(pair);
		}
		HttpUriRequest reqMethod = null;
		if ("post".equals(method)) {
			reqMethod = RequestBuilder
					.post()
					.setUri(url)
					.addParameters(
							params.toArray(new BasicNameValuePair[params.size()]))
					.setConfig(requestConfig).build();
		} else if ("get".equals(method)) {
			reqMethod = RequestBuilder
					.get()
					.setUri(url)
					.addParameters(
							params.toArray(new BasicNameValuePair[params.size()]))
					.setConfig(requestConfig).build();
		}
		return reqMethod;
	}

	/**
	 * 从服务器中拉取数据
	 * @param appKey
	 * @return
	 * @throws IOException
	 */
	public static Map<String,List<InterfaceControl>> getFromServer(String appKey)
			throws IOException {
		String url=IP+":"+PORT+"/interface/pull/"+appKey;
		HashMap<String, List<InterfaceControl>> result = new HashMap<String,List<InterfaceControl>>();
		Map<String, String> map = new HashMap<String, String>();
		InterfaceControl overallControl=null;
		List<InterfaceControl> interfaces=new ArrayList<InterfaceControl>();
		HttpClient client = getHttpClient();
		HttpUriRequest post = getRequestMethod(map, url, "post");
		HttpResponse response = client.execute(post);

		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity, "utf-8");
			overallControl = InterfaceControl.parseOverall(message);
			interfaces = InterfaceControl.parseCommon(message);
			List<InterfaceControl> oc = new ArrayList<InterfaceControl>();
			oc.add(overallControl);
			result.put("overallControl", oc);
			result.put("interfaces", interfaces);
		}
		return result;
	}

	/**
	 * 从服务器中拉取数据
	 * @param appKey
	 * @return
	 * @throws IOException
	 */
	public static List<Blacklist> getBlackListFromServer(String appKey)
			throws IOException {
		String url=IP+":"+PORT+"/blacklist/pull";
		Map<String, String> map = new HashMap<String, String>();
		map.put("appKey", appKey);
		List<Blacklist> Blacklists=new ArrayList<Blacklist>();
		HttpClient client = getHttpClient();
		HttpUriRequest post = getRequestMethod(map, url, "post");
		HttpResponse response = client.execute(post);

		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity, "utf-8");
			Blacklists = Blacklist.parse(message);
		}
		return Blacklists;
	}
	
	/**
	 * 向服务器更新黑名单数据
	 * @param appKey
	 * @param blacklists
	 * @return
	 * @throws IOException
	 */
	public static boolean updateBlackListToServer(String appKey,List<Blacklist> blacklists)
			throws IOException {
		String url=IP+":"+PORT+"/blacklist/update";
		Map<String, String> map = new HashMap<String, String>();
		JSONArray jsonArray = new JSONArray();
		jsonArray.addAll(blacklists);
		map.put("blacklists", jsonArray.toJSONString());
		map.put("appKey", appKey);
		HttpClient client = getHttpClient();
		HttpUriRequest post = getRequestMethod(map, url, "post");
		HttpResponse response = client.execute(post);

		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity, "utf-8");
			System.out.println(message);
			JSONObject jsonObject = (JSONObject)JSONObject.parse(message);
			if(jsonObject.get("success").toString().equals("1"))
				return true;
		}
		return false;
	}
	public static void main(String[] args) {
		try {
			List<Blacklist> blackListFromServer = getBlackListFromServer("123456");
			blackListFromServer.get(0).setSecDate(new Timestamp(System.currentTimeMillis()));
			blackListFromServer.get(0).setTimes((short)2);;
			Blacklist blacklist = new Blacklist();
			blacklist.setAppKey("123456");
			blacklist.setFirDate(new Timestamp(System.currentTimeMillis()));
			blacklist.setLimitedIp("127.0.0.1");
			blacklist.setTimes((short)1);
			blacklist.setUsername("test");
			blackListFromServer.add(blacklist);
			updateBlackListToServer("123456",blackListFromServer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static List<InterfaceControl> getInterfacesTest(){
		List<InterfaceControl> interfaces=new ArrayList<InterfaceControl>();
		InterfaceControl ic=new InterfaceControl();
		ic.setApi_name("/user/add");
		ic.setInterface_id(1);
		ic.setFrequency(100);
		ic.setTimeout(1);
		ic.setUnit('h');
		interfaces.add(ic);
		ic=new InterfaceControl();
		ic.setApi_name("/user/list");
		ic.setInterface_id(2);
		ic.setFrequency(100);
		ic.setTimeout(100);
		ic.setUnit('s');
		interfaces.add(ic);
		return interfaces;
	}
	
	public static List<Blacklist> getBlacklistsTest(){
		List<Blacklist> blacklists=new ArrayList<Blacklist>();
		Blacklist b=new Blacklist();
		b.setLimitedIp("123.121.121.22");
		blacklists.add(b);
		b=new Blacklist();
		b.setUsername("xiao");
		blacklists.add(b);
		return blacklists;
	}

}
