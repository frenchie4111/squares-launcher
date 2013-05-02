package org.mikelyons.squares;

/*
 * Takes a view and populates it with a list of all the apps
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import com.deaux.fan.FanView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AllAppLoader {
	
	Context c;
	LinearLayout fanView;
	FanView fan;
	BoxHandlerModel model;
	PackageManager pkg;
	
	private HashMap<String, ApplicationInfo> apps;
	
	public AllAppLoader(Context c, LinearLayout fanView, FanView fan, BoxHandlerModel model) {
		setApps(new HashMap<String, ApplicationInfo>());
		this.fanView = fanView;
		this.fan = fan;
		this.c = c;
		this.model = model;
		pkg = c.getPackageManager();
		showLoading();
		new AllAppLoaderAsync().doInBackground(this);
	}
	
	public HashMap<String, ApplicationInfo> getMap() {
		return apps;
	}

	public void setApps(HashMap<String, ApplicationInfo> apps) {
		this.apps = apps;
	}
	
	public void showLoading() {
		TextView tv = new TextView(c);
		tv.setText("Loading...");
		fanView.addView(tv);
	}
	
	public void populateList() {
		fanView.removeAllViews();
		final HashMap<String, ApplicationInfo> names = getMap();
		TreeSet<String> keys = new TreeSet<String>(names.keySet());
		
		for( final String key : keys ) {
			
			LinearLayout button_icon = new LinearLayout(c);
			button_icon.setOrientation(LinearLayout.HORIZONTAL);
			button_icon.setLayoutParams(new LayoutParams(200, 75));
			
			ImageView icon_view = new ImageView(c);
			icon_view.setLayoutParams(new LayoutParams(75,75));
			icon_view.setImageDrawable( names.get(key).getIcon(pkg) );
			button_icon.addView(icon_view);
			
			TextView app_name=  new TextView(c);
			app_name.setText(key);
			app_name.setLayoutParams(new LayoutParams(150, 75));
			button_icon.addView(app_name);
			
			button_icon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if( fan.isOpen() ) {
						names.get(key).start(c);
					}
				}
			});
			
			
			button_icon.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if( event.getAction() == MotionEvent.ACTION_DOWN ) {
						v.setBackgroundColor(Color.YELLOW);						
					} else {
						v.setBackgroundColor(Color.BLACK);
					}
					return false;
				}
			});
			
			button_icon.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					FanViewController.addBoxWithDialog( c, names.get(key), model );
					return false;
				}
			});
			
			fanView.addView(button_icon);
		}
	}
	
	public PackageManager getPkg() {
		return pkg;
	}
}
