package com.mayerdan.nothingcalendar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Users extends Activity {
	
		//production settings
		//private static final String HOST   = "nothingcalendar.com";
		
		//development settings
		private static final String HOST   = "legal-hate.showoff.io";
		
		private static final String SERVER = "http://"+HOST;
		private static final String TAG = "Main";
		
		Map<String, String> extraHeaders = new HashMap<String, String>();
        //Map<String, String> userData = new HashMap<String, String>();
		Map<String,Object> userData = new HashMap<String, Object>();
		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        extraHeaders.put("app-request", "true");
	    	
	        setContentView(R.layout.main);
	        WebView webview = (WebView) findViewById(R.id.webview);
	        webview.setWebViewClient(new MyWebViewClient());
	        webview.addJavascriptInterface(new JavaScriptInterface(this), "Android");
	        WebSettings webSettings = webview.getSettings();
	        webSettings.setJavaScriptEnabled(true);
	    
	        //simple mustache example
//	        String text = "One, two, {{three}}. Three sir!";
//	        Template tmpl = Mustache.compiler().compile(text);
//	        Map<String, String> data = new HashMap<String, String>();
//	        data.put("three", "five");
//	        //Log.d("json",tmpl.execute(data));
//	        webview.loadDataWithBaseURL(SERVER,tmpl.execute(data),"text/html","UTF-8","about:blank");
//	        //webview.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl)
//	        
	        //load html from web
	        //webview.loadUrl(SERVER+"/users", extraHeaders);
	        
	        executeAsyncTask();
	        
	    }
	    
	    private void executeAsyncTask(){
	    	Log.i(TAG, "starting async");
	    	GetUsersAyncTask async=new GetUsersAyncTask();
	    	Hashtable<String,String> ht=new Hashtable<String,String>();
	    	async.execute(ht);
	    }
	 
	    private class GetUsersAyncTask extends AsyncTask<Map<String, String>,Void,String>{
	 
	          @Override
	          protected String doInBackground(Map<String, String>... params) {
	          //unused get options
	          Hashtable<String,String> ht=new Hashtable<String,String>();
	 
	          Log.i(TAG, "about to get");
	           String json = HelperHttp.getJSONResponseFromURL(SERVER+"/users.json", ht);
	           Log.i(TAG, "json string: "+json);
	           if(json!=null) {
	             parseJsonString(json.toString());
	           } else {
	             return "Invalid Json request";
	           }
	           return "SUCCESS";
	          }
	          
	          @SuppressWarnings("unchecked")
			protected void parseJsonString(String json){
	              try {
	                     	  
	            	  ObjectMapper mapper = new ObjectMapper();
	            	  //Object root = mapper.readValue(json, Object.class);
	            	  //Map<String,String> rootAsMap = mapper.readValue(json, Map.class);
	            	  //rails 3.0.3 is bad and doesn't qoute strings
	            	  userData = (Map<String,Object>) mapper.readValue(json, Map.class);
	            		 
	                  } catch (JsonParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	 
	          } 
	 
	          @Override
	          protected void onPostExecute(String result){
	        	  Log.i(TAG, "post execute: "+result);
	              if(result=="SUCCESS")
	              {
	                String text = "<div class='user_list'>"+
	                		"<ul>"+
	                		"{{#users}}"+
	                		"<li><a href='/users/{{to_param}}' data-pjax='#main'>{{name}}</a></li>"+
	                		"{{/users}}"+
	                		"</ul>"+
	                		"</div>";
	      	        Template tmpl = Mustache.compiler().compile(text);
   	        	    WebView webview = (WebView) findViewById(R.id.webview);
	      	        webview.loadDataWithBaseURL(SERVER,tmpl.execute(userData),"text/html","UTF-8","about:blank");
	      	        Log.i(TAG, "display complete");
	      	        Toast.makeText(Users.this, "display complete", Toast.LENGTH_SHORT).show();
	              }
	              else {
	            	  Toast.makeText(Users.this, "error on json parse", Toast.LENGTH_SHORT).show();
	              }
	          }
	 
	         }

	    
	    public class MyWebViewClient extends WebViewClient {
	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            if (Uri.parse(url).getHost().equals(HOST)) {
	                // This is my web site, so do not override; let my WebView load the page
	            	view.loadUrl(url, extraHeaders);
	                return true;
	            }
	            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
	            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	            startActivity(intent);
	            return true;
	        }
	    }
	    
	    public class JavaScriptInterface {
	        Context mContext;

	        /** Instantiate the interface and set the context */
	        JavaScriptInterface(Context c) {
	            mContext = c;
	        }

	        /** Show a toast from the web page */
	        public void showToast(String toast) {
	        	Log.i(TAG, "hit toast");
	            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	        }
	    }

}
