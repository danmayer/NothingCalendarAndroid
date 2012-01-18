package com.mayerdan.nothingcalendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserArrayAdapter extends ArrayAdapter<Object> {
	
	private static final String tag = "CountryArrayAdapter";
	private static final String ASSETS_DIR = "images/";
	private Context context;
	private ImageView userIcon;
	private TextView userName;
	private TextView userId;
	private List<Object> users = new ArrayList<Object>();

	public UserArrayAdapter(Context context, int textViewResourceId,
			List<Object> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.users = objects;
	}
	
	public int getCount() {
		return this.users.size();
	}

	public LinkedHashMap getItem(int index) {
		return (LinkedHashMap) this.users.get(index);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			// ROW INFLATION
			Log.d(tag, "Starting XML Row Inflation ... ");
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.user_listitem, parent, false);
			Log.d(tag, "Successfully completed XML Row Inflation!");
		}

		// Get item
		LinkedHashMap user = getItem(position);
		
		// Get reference to ImageView 
		userIcon = (ImageView) row.findViewById(R.id.user_icon);
		
		// Get reference to TextView - country_name
		userName = (TextView) row.findViewById(R.id.user_name);
		
		// Get reference to TextView - country_abbrev
		userId = (TextView) row.findViewById(R.id.user_id);

		//Set user name
		//userName.setText(user.name);
		userName.setText((String)user.get("name"));
		
		// Set user icon usign File path
//		String imgFilePath = ASSETS_DIR + country.resourceId;
//		try {
//			Bitmap bitmap = BitmapFactory.decodeStream(this.context.getResources().getAssets()
//					.open(imgFilePath));
//			countryIcon.setImageBitmap(bitmap);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		// Set user Id
		//userId.setText(user.id);
		userId.setText((String)user.get("id"));
		return row;
	}
	
}