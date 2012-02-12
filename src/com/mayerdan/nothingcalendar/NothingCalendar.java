package com.mayerdan.nothingcalendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class NothingCalendar extends Activity {

	//production settings
	//public static final String HOST   = "nothingcalendar.com";
	
	//development settings
	//public static final String HOST   = "legal-hate.showoff.io";
	public static final String HOST   = "10.0.2.2:3000";
	
	public static final String SERVER = "http://"+HOST;
	
	private static final String TAG = "NC Main";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "oncreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         

        setContentView(R.layout.main);
        WebView webview = (WebView) findViewById(R.id.webview);
        NCViewClient mainwvClient = new NCViewClient(this);
        webview.setWebViewClient(mainwvClient);
        webview.addJavascriptInterface(new NCJavaScriptInterface(this), "Android");
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.loadUrl(SERVER, mainwvClient.getExtraHeaders());
        //webview.loadUrl("javascript:Android.showToast('moo cow say 2')");
    }
    
}

