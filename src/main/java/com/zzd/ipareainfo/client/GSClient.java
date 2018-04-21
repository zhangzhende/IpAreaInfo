package com.zzd.ipareainfo.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GSClient {
	private static final Logger logger = LoggerFactory
			.getLogger(GSClient.class);
	static HttpClient httpClient = null;
	// 构造HttpClient的实例
	private static GSClient instance;

	private GSClient() {
		httpClient = new HttpClient();
	}

	public static GSClient getInstance() {
		if (instance == null) {
			return new GSClient();
		}
		return instance;
	}

	public String getMethod(String url) throws HttpException, IOException {
		GetMethod getMethod = new GetMethod(url);
		String resposeData = "";
			HttpConnectionManagerParams managerParams = httpClient
					.getHttpConnectionManager().getParams();
			// 设置连接超时时间(单位毫秒)
			managerParams.setConnectionTimeout(1200000);
			// 设置读数据超时时间(单位毫秒) 等待结果时间
			managerParams.setSoTimeout(1200000);
			// 执行postMethod，并返回状态码
			int statusCode = httpClient.executeMethod(getMethod);
			logger.info("statusCode = " + statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				logger.info("接口调用成功");
				resposeData = getMethod.getResponseBodyAsString();
			} else {
				logger.info("接口返回失败!");
			}
		return resposeData;
	}
	public String postMethod(String url, String content) {
		PostMethod postMethod = new PostMethod(url);
		try {
			postMethod.addRequestHeader("Content-Type", "application/json");
			postMethod.setRequestEntity(new StringRequestEntity(content,
					"text/json", "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("调用异常",e);
		}
		String resposeData = "";
		try {
			HttpConnectionManagerParams managerParams = httpClient
					.getHttpConnectionManager().getParams();
			// 设置连接超时时间(单位毫秒)
			managerParams.setConnectionTimeout(1200000);
			// 设置读数据超时时间(单位毫秒) 等待结果时间
			managerParams.setSoTimeout(1200000);
			// 执行postMethod，并返回状态码
			int statusCode = httpClient.executeMethod(postMethod);
			logger.info("statusCode = " + statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				logger.info("接口调用成功");
				resposeData = postMethod.getResponseBodyAsString();
			} else {
				logger.info("接口返回失败!");
			}
		} catch (IOException e) {
			logger.error("调用接口异常",e);
		}
		return resposeData;
	}
}
