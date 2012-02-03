package com.mayerdan.nothingcalendar;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.util.Log;

public class HelperHttpClient {

	public static HttpClient httpclient;

	private static List<NameValuePair> buildNameValuePair(Hashtable<String, String> httpPost){
		if(httpPost==null) return null;

		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		Enumeration<String> keys=httpPost.keys();
		while(keys.hasMoreElements()){
			String key = (String)keys.nextElement();
			String value = (String)httpPost.get(key);
			BasicNameValuePair nv=new BasicNameValuePair(key,value);
			nvps.add(nv);
		}

		return nvps;
	}


	private static String buildGetUrl(List<NameValuePair> params, String url){
		String paramString = URLEncodedUtils.format(params, "utf-8");
		if(!url.endsWith("?"))
			url += "?";

		url+=paramString;
		return url;
	}

	public static DefaultHttpClient getThreadSafeClient() {
		if (httpclient != null)
			return (DefaultHttpClient) httpclient;
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 100);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		// Create and initialize scheme registry 
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(
				new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));


		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
		httpclient = new DefaultHttpClient(cm, params);        

		return (DefaultHttpClient) httpclient;
	}



	public static String getJSONResponseFromURL(String url, Hashtable<String, String> httpGetParams){
		String json_string="";
		List<NameValuePair> nvps=buildNameValuePair(httpGetParams);
		url=buildGetUrl(nvps,url);
		System.out.println("URL==>"+url);
		InputStream is = null;
		try{
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = getThreadSafeClient().execute(httpget);

			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			BufferedReader reader=new BufferedReader(new InputStreamReader(is),8192);
			String line=null;
			while((line=reader.readLine())!=null){
				json_string=json_string+line;
			}
			response.getEntity().consumeContent();
			System.out.println("Json Response==>"+json_string);
		}catch(Exception e){
			Log.e("log_tag", "Error in http connection"+e.toString());
			return null;
		}
		return json_string;
	}


}
