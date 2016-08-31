package com.sense.test.utils;

import java.util.Map;
/**
 * 封装HttpClient的响应数据
 * @author kanghl
 * @email kanghl@sense.com.cn
 * @date 2016年7月8日
 */
public class CloudResponse {

	/**
	 * 返回的数据类型
	 */
	private String dataType;
	/**
	 * 返回的数据
	 */
	private String data;
	
	/**
	 * 响应头
	 */
	private Map<String, String> headers;
	
	/**
	 * 返回的状态码
	 */
	private int code;
	

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public String getHeader(String name){
		return this.getHeaders().get(name);
	}
	
}
