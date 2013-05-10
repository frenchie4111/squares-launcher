package org.mikelyons.squares;

/*
 * Takes a view and populates it with a list of all the apps
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import com.deaux.fan.FanView;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
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
	WidgetController wc;
	
	private HashMap<String, ApplicationInfo> apps;
	
	public AllAppLoader(Context c, LinearLayout fanView, FanView fan, WidgetController wc, BoxHandlerModel model) {
		setApps(new HashMap<String, ApplicationInfo>());
		this.fanView = fanView;
		this.fan = fan;
		this.c = c;
		this.model = model;
		this.wc = wc;
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
		
		// Add add widget button
		
		LinearLayout wc_button_icon = new LinearLayout(c);
		wc_button_icon.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams wc_button_icon_lp = new LinearLayout.LayoutParams( 200, 75 );
		wc_button_icon_lp.setMargins(0, 5, 0, 5);
		wc_button_icon.setLayoutParams(wc_button_icon_lp);
		
		ImageView wc_icon_view = new ImageView(c);
		wc_icon_view.setLayoutParams(new LayoutParams(75,75));
		wc_icon_view.setImageResource( R.drawable.logo1 );
		wc_button_icon.addView(wc_icon_view);
		
		TextView wc_app_name=  new TextView(c);
		wc_app_name.setText( "Add Widget" );
		LinearLayout.LayoutParams wc_app_name_lp = new LinearLayout.LayoutParams(150, 75);
		wc_app_name.setLayoutParams(wc_app_name_lp );
		wc_app_name.setGravity(Gravity.CENTER_VERTICAL);
		wc_button_icon.addView(wc_app_name);
		
		wc_button_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if( fan.isOpen() ) {
					wc.showDialog();
				}
			}
		});
		
		fanView.addView( wc_button_icon );
		
		final HashMap<String, ApplicationInfo> names = getMap();
		TreeSet<String> keys = new TreeSet<String>(names.keySet());
		
		for( final String key : keys ) {
			
			LinearLayout button_icon = new LinearLayout(c);
			button_icon.setOrientation(LinearLayout.HORIZONTAL);
			button_icon.setLayoutParams(new LayoutParams(200, 75));
			
			final ImageView icon_view = new ImageView(c);
			icon_view.setLayoutParams(new LayoutParams(75,75));
			icon_view.setImageDrawable( names.get(key).getIcon(pkg) );
			button_icon.addView(icon_view);
			
			TextView app_name=  new TextView(c);
			app_name.setText(key);
			app_name.setLayoutParams(new LayoutParams(150, 75));
			app_name.setGravity(Gravity.CENTER_VERTICAL);
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
					// Set position
					
					
					// Change color
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
					//FanViewController.addBoxWithDialog( c, names.get(key), model );
					//FanViewController.addBoxWithDrag(c, names.get(key), model, (int) v.getX(), (int) v.getY());
					((MainActivity) c).hideMenu();
					
					ClipData data = ClipData.newPlainText("data", "asdf");
					DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(icon_view);
					v.startDrag(data, shadowBuilder, v, 0);
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
