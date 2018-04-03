package com.hexun.attention.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * http工具类
 *
 * @version 1.0.0
 * @date 2016-06-06
 * @Copyright 版权所有 (c) 2016 hexun
 * @author Zhang Yang
 */
public class HttpUtil {

	private static CloseableHttpClient httpClient = HttpClients.createDefault();
	
	private static HttpClientContext context = new HttpClientContext();
	
	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return
	 */
	public static String sendGet(String url) {
		CloseableHttpResponse response = null;
		String content = null;
		try {
			HttpGet get = new HttpGet(url);
			get.addHeader("Accept-Encoding", "\t");
			response = httpClient.execute(get, context);
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			if (response != null) {
				try {
					response.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return content;
	}
	
}
