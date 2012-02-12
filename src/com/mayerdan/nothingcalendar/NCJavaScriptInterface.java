package com.mayerdan.nothingcalendar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NCJavaScriptInterface {

	private static final String TAG = "JSinterface";
	
	Context mContext;

	/** Instantiate the interface and set the context */
	NCJavaScriptInterface(Context c) {
		mContext = c;
	}

	/** Show a toast from the web page */
	public void showToast(String toast) {
		Log.i(TAG, "hit toast");
		Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	}

	public void showUsers() {
		Intent myIntent = new Intent(mContext, Users.class);
		mContext.startActivity(myIntent);
	}
	
	public void showAbout() {
		Intent myIntent = new Intent(mContext, About.class);
		mContext.startActivity(myIntent);
	}
}
