package com.mayerdan.nothingcalendar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

/*
 * Displays about page as mustache in webview
 */
public class About extends Activity {

	private static final String SERVER = "http://"+NothingCalendar.HOST;
	private static final String TAG = "Main";

	Map<String, String> extraHeaders = new HashMap<String, String>();
	//Map<String, String> userData = new HashMap<String, String>();
	Map<String,Object> userData = new HashMap<String, Object>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		extraHeaders.put("app-request", "true");

		setContentView(R.layout.main);
	
		WebView webview = (WebView) findViewById(R.id.webview);
		NCViewClient mainwvClient = new NCViewClient(this);
		webview.setWebViewClient(mainwvClient);
		webview.addJavascriptInterface(new NCJavaScriptInterface(this), "Android");
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);

		//render html from server
		webview.loadUrl(SERVER+"/site/about", mainwvClient.getExtraHeaders());
		
		//mustache that to webview
		//executeAsyncTask();

	}

	private void executeAsyncTask(){
		Log.i(TAG, "starting async");
		GetAboutAyncTask async=new GetAboutAyncTask();
		Hashtable<String,String> ht=new Hashtable<String,String>();
		async.execute(ht);
	}

	private class GetAboutAyncTask extends AsyncTask<Map<String, String>,Void,String>{

		@Override
		protected String doInBackground(Map<String, String>... params) {
			//unused get options
			Hashtable<String,String> ht=new Hashtable<String,String>();

			Log.i(TAG, "about to get");
			String json = HelperHttpClient.getJSONResponseFromURL(SERVER+"/site/about", ht);
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
				userData = mapper.readValue(json, Map.class);

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
				//	                String text = "<div class='user_list'>"+
				//	                		"<ul>"+
				//	                		"{{#users}}"+
				//	                		"<li><a href='/users/{{to_param}}' data-pjax='#main'>{{name}}</a></li>"+
				//	                		"{{/users}}"+
				//	                		"</ul>"+
				//	                		"</div>";
				//	      	        Template tmpl = Mustache.compiler().compile(text);
				//   	        	    WebView webview = (WebView) findViewById(R.id.webview);
				//	      	        webview.loadDataWithBaseURL(SERVER,tmpl.execute(userData),"text/html","UTF-8","about:blank");

				// Set the View layer
				setContentView(R.layout.main);

				Log.i(TAG, "display complete");
				Toast.makeText(About.this, "display complete", Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(About.this, "error on json parse", Toast.LENGTH_SHORT).show();
			}
		}

	}

}
