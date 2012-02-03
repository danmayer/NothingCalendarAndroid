package com.mayerdan.nothingcalendar;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

 public class NCViewClient extends WebViewClient {
	 
		private Activity activity;
		public Map<String, String> extraHeaders = new HashMap<String, String>();
		
        public NCViewClient(Activity initialActivity) {
        	this.activity = initialActivity;
        	extraHeaders.put("app-request", "true");
		}
        
        public Map<String, String> getExtraHeaders() {
        	return extraHeaders;
        }

		@Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals(NothingCalendar.HOST)) {
                // This is my web site, so do not override; let my WebView load the page
            	view.loadUrl(url, extraHeaders);
                return true;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
            return true;
        }
}