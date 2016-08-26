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

import com.cn21.frequencyControl_public.Blacklist;
import com.cn21.frequencyControl_public.Interfac;

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
	 * @param url
	 * @param method
	 * @return
	 * @throws IOException
	 */
	public static List<Interfac> getFromServer(String url, String method)
			throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		List<Interfac> interfaces=new ArrayList<Interfac>();
		HttpClient client = getHttpClient();
		HttpUriRequest post = getRequestMethod(map, url, method);
		HttpResponse response = client.execute(post);

		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity, "utf-8");
			interfaces = Interfac.parse(message);
		}
		return interfaces;
	}
	/**
	 * 从服务器中拉取数据
	 * @param url
	 * @param method
	 * @return
	 * @throws IOException
	 */
	public static List<Blacklist> getFromServer1(String url, String method)
			throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		List<Blacklist> Blacklists=new ArrayList<Blacklist>();
		HttpClient client = getHttpClient();
		HttpUriRequest post = getRequestMethod(map, url, method);
		HttpResponse response = client.execute(post);

		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity, "utf-8");
			Blacklists = Blacklist.parse(message);
		}
		return Blacklists;
	}

	public static void main(String[] args) {
		String url=IP+":"+PORT+"/FrequencyControl/blacklist/pull/weqeqweq";
		try {
			System.out.println(getFromServer1(url, "get"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
