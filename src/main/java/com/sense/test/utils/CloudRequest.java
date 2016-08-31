package com.sense.test.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装HttpClient的请求信息
 * @author kanghl
 * @email kanghl@sense.com.cn
 * @date 2016年7月8日
 */

public class CloudRequest {

	/**
	 * 请求地址
	 */
	private String uri;
	/**
	 * 请求的编码，默认为UTF-8
	 */
	private Charset charset=Charset.forName("UTF-8");
	/**
	 * 请求头设置
	 */
	private Map<String, String> headers=new HashMap<String, String>();
	/**
	 * 请求参数配置
	 */
	private Map<String, String> params=new HashMap<String, String>();

	/**
	 * 请求类型，现在只支持Get、Post
	 */
	private RequestType type;

	private DataType dataType=DataType.FORM_DATA;
	/**
	 * 上传的文件
	 */
	private Map<String, File> files;
	
	public RequestType getType() {
		return type;
	}

	public void setType(RequestType type) {
		this.type = type;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void addHeader(String name,String value) {
		headers.put(name, value);
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void addParam(String name,String value) {
		params.put(name, value);
	}

	public void addFile(String name,File file){
		if(files==null){
			files=new HashMap<String, File>();
		}
		files.put(name,file);
	}
	
	public Map<String, File> getFiles(){
		return this.files;
	}
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
}
