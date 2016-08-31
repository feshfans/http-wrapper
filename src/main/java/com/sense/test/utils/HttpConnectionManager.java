package com.sense.test.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 一个简单的HttpClient连接池的管理
 * @author Administrator
 *
 */

public class HttpConnectionManager {
   	private static HttpParams httpParams;
	private static PoolingClientConnectionManager cm;
	/**
	 * 最大连接数
	 */
	private int maxTotalConnections = 200;
	/**
	 * 连接超时时间
	 */
	private final static int CONNECT_TIMEOUT = 1000;
	/**
	 * 读取超时时间
	 */
	private final static int READ_TIMEOUT = 10000;

	public HttpConnectionManager(){
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(
				new Scheme("http",80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(
				new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
		cm = new PoolingClientConnectionManager(schemeRegistry);
		cm.setMaxTotal(maxTotalConnections);
		cm.setDefaultMaxPerRoute(20);

		httpParams = new BasicHttpParams();
		httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,CONNECT_TIMEOUT);
		httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, READ_TIMEOUT);
	}

	public static HttpClient getHttpClient() {
		return new DefaultHttpClient(cm, httpParams);
	}

	public static HttpClient getHttpsClient() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext ctx = SSLContext.getInstance("TLS");
		X509TrustManager tm = new X509TrustManager() {

			public void checkClientTrusted(X509Certificate[] chain,
										   String authType) throws CertificateException
			{
				// TODO Auto-generated method stub

			}

			public void checkServerTrusted(X509Certificate[] chain,
										   String authType) throws CertificateException
			{
				// TODO Auto-generated method stub

			}

			public X509Certificate[] getAcceptedIssuers()
			{
				// TODO Auto-generated method stub
				return null;
			}

		};

		HttpClient base=getHttpClient();
		ctx.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory ssf = new SSLSocketFactory(ctx);
		ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		ClientConnectionManager ccm = base.getConnectionManager();
		SchemeRegistry sr = ccm.getSchemeRegistry();
		//设置要使用的端口，默认是443
		sr.register(new Scheme("https", ssf, 443));
		return new DefaultHttpClient(ccm, base.getParams());
	}

	public int getMaxTotalConnections() {
		return maxTotalConnections;
	}

	public void setMaxTotalConnections(int maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

}
