package com.mayerdan.nothingcalendar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.Toast;

public class Users extends ListActivity {

	private static final String SERVER = "http://"+NothingCalendar.HOST;
	private static final String TAG = "Main";

	Map<String, String> extraHeaders = new HashMap<String, String>();
	//Map<String, String> userData = new HashMap<String, String>();
	Map<String,Object> userData = new HashMap<String, Object>();
	
	// Create a customized ArrayAdapter
	UserArrayAdapter adapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		extraHeaders.put("app-request", "true");
		executeAsyncTask();
		//setListAdapter(adapter);
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // Get the item that was clicked
        Object o = getListAdapter().getItem(position);
        String keyword = o.toString();
        Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG)
                .show();


        {

        } 
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
			String json = HelperHttpClient.getJSONResponseFromURL(SERVER+"/users.json", ht);
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
				//setContentView(R.layout.users);
				//setTitle("TestIconizedListView");

				// Create Parser for raw/countries.xml
				//	          		CountryParser countryParser = new CountryParser();
				//	          		InputStream inputStream = getResources().openRawResource(
				//	          				R.raw.countries);

				// Parse the inputstream
				//countryParser.parse(inputStream);

				// Get Countries
				//users = userData.get("users");
				List<Object> users = (List<Object>) userData.get("users");
				//List<Country> countryList = countryParser.getList();


				// Create a customized ArrayAdapter
				adapter = new UserArrayAdapter(
						getApplicationContext(), R.layout.user_listitem, users);
				
				setListAdapter(adapter);

				// Get reference to ListView holder
				//ListView lv = getListView();
				// Set the ListView adapter
				//lv.setAdapter(adapter);

				Log.i(TAG, "display complete");
				Toast.makeText(Users.this, "display complete", Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(Users.this, "error on json parse", Toast.LENGTH_SHORT).show();
			}
		}

	}

}
