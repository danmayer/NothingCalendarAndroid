package com.mayerdan.nothingcalendar;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NothingCalendar extends Activity {

	//production settings
	//private static final String HOST   = "nothingcalendar.com";
	
	//development settings
	private static final String HOST   = "legal-hate.showoff.io";
	
	private static final String SERVER = "http://"+HOST;
	private static final String TAG = "Main";
	
	Map<String, String> extraHeaders = new HashMap<String, String>();
	
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
        webview.loadUrl(SERVER, extraHeaders);
        //webview.loadUrl("javascript:Android.showToast('moo cow say 2')");
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
        
        public void showUsers() {
        	Intent myIntent = new Intent(NothingCalendar.this, Users.class);
        	NothingCalendar.this.startActivity(myIntent);
        }
    }
    
}

