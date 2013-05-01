package org.mikelyons.squares;

import java.util.HashMap;
import java.util.TreeSet;

import com.deaux.fan.FanView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FanViewController {
	
	Context c;
	
	LinearLayout fanView;
	FanView fan;
	
	AllAppLoader mAppLoader;
	
	BoxHandlerModel model;
	
	boolean hovering;
	
	public FanViewController( Context c, LinearLayout fanView, FanView fan, BoxHandlerModel model ) {
		this.c = c;
		this.fanView = fanView;
		populateList();
		this.fan = fan;
		this.model = model;
	}
	
	private void populateList() {
		
		mAppLoader = new AllAppLoader();
		
		PackageManager pkg = c.getPackageManager();
		
		mAppLoader.loadApps(pkg);
		
		final HashMap<String, ApplicationInfo> names = mAppLoader.getMap(pkg);
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
					addBoxWithDialog( names.get(key) );
					fan.showMenu();
					return false;
				}
			});
			
			fanView.addView(button_icon);
		}
	}
	
	public void addBoxWithDialog( ApplicationInfo info ) {
		LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View layout = inflater.inflate(R.layout.new_icon_dialog, (ViewGroup) ((Activity) c).findViewById(R.id.newAppDialogRoot));
	    
	    
	    
	    AlertDialog.Builder builder = new AlertDialog.Builder(c).setView(layout);
	    
	    builder.setTitle("Adding App: " + info.getName(c.getPackageManager()) );
	    
	    AlertDialog alertDialog = builder.create();
	    alertDialog.show();
		model.addBox(info, 1, 0, 200, 150, true);
	}
	
	public void setBoxModel( BoxHandlerModel model ) {
		this.model = model;
	}
}
