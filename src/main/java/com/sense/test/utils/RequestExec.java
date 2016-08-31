package com.sense.test.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import sun.misc.Cleaner;


public class RequestExec {

	private CloudRequest request;
	
	public RequestExec(CloudRequest request) {
		this.request=request;
	}
	
	public CloudResponse exec() throws ClientProtocolException, IOException, ParseException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException {
		CloudResponse response=new CloudResponse();
		
		HttpClient client=null;
		if(request.getUri().toLowerCase().startsWith("https")){
			client=HttpConnectionManager.getHttpsClient();
		}else{
			client=HttpConnectionManager.getHttpClient();
		}


		HttpResponse httpResponse=null;
		
		if(request.getType()==RequestType.GET){
			httpResponse=get(client,request);
		}else{
			httpResponse=post(client,request);
		}
		
		HttpEntity entity= httpResponse.getEntity();
		response.setData(EntityUtils.toString(entity));
		response.setDataType(EntityUtils.getContentMimeType(entity));
		response.setCode(httpResponse.getStatusLine().getStatusCode());
		
		org.apache.http.Header[] headers= httpResponse.getAllHeaders();
		
		Map<String, String> map=new HashMap<String, String>();
		for(org.apache.http.Header header: headers){
			
			map.put(header.getName(), header.getValue());
		}
		response.setHeaders(map);
		return response;
	}
	
	private HttpResponse post(HttpClient client,CloudRequest request) throws ClientProtocolException, IOException{
		HttpPost post=new HttpPost(request.getUri());
		
		if(request.getHeaders()!=null){
			for(Map.Entry<String, String> entry:request.getHeaders().entrySet()){
				post.addHeader(entry.getKey(), entry.getValue());
			}
		}
		
		Map<String, File> files=request.getFiles();
		if(files!=null){
			MultipartEntity multipartEntity = new MultipartEntity();
			for(Map.Entry<String, File> entry : files.entrySet()){
				ContentBody body=new FileBody(entry.getValue());
				multipartEntity.addPart(entry.getKey(), body);
			}
			if(request.getParams()!=null){
				for(Map.Entry<String, String> entry:request.getParams().entrySet()){
					multipartEntity.addPart(entry.getKey(),new StringBody(entry.getValue(), request.getCharset()));
				}
			}
			post.setEntity(multipartEntity);
			
			
		}else{
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			if(request.getParams()!=null){
				for(Map.Entry<String, String> entry:request.getParams().entrySet()){
					//requestMethod.getParams().setParameter(entry.getKey(), entry.getValue());
					params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			post.setEntity(new UrlEncodedFormEntity(params, request.getCharset()));
		}

		if(request.getDataType()==DataType.RAW){
			StringEntity postingString = new StringEntity(request.getParams().get("json"));// json传递
			post.setEntity(postingString);
			post.setHeader("Content-Type", "application/json");
		}
		return client.execute(post);

		
	}
	
	private HttpResponse get(HttpClient client,CloudRequest request) throws ParseException, URISyntaxException, IOException{
		HttpGet get=new HttpGet();
		if(request.getHeaders()!=null){
			for(Map.Entry<String, String> entry:request.getHeaders().entrySet()){
				get.addHeader(entry.getKey(), entry.getValue());
			}
		}

		List<NameValuePair> params=new ArrayList<NameValuePair>();
		if(request.getParams()!=null){
			for(Map.Entry<String, String> entry:request.getParams().entrySet()){
				//requestMethod.getParams().setParameter(entry.getKey(), entry.getValue());
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		get.setURI(new URI(request.getUri()+"?"+EntityUtils.toString(new UrlEncodedFormEntity(params,request.getCharset()))));
		
		return client.execute(get);
	}
}
