package org.mikelyons.squares;

import java.util.ArrayList;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class WidgetController {
	
	Context c;
	
	AppWidgetManager mAppWidgetManager;
	AppWidgetHost mAppWidgetHost;
	
	// Temporary
	RelativeLayout layout;
	
	int appWidgetId;
	
	public static int WIDGET_ID = 4111;
	
	public static int REQUEST_PICK_APPWIDGET = 41;
	public static int REQUEST_ADD_APPWIDGET = 42;
	
	public WidgetController(final Context c) {
		this.c = c;
		appWidgetId = 0;
		mAppWidgetManager = AppWidgetManager.getInstance(c);
		mAppWidgetHost = new AppWidgetHost(c, WIDGET_ID);
		mAppWidgetHost.startListening();
	}
	
	public void setLayout(RelativeLayout layout ) {
		this.layout = layout;
	}
	
	public void showDialog() {
		appWidgetId = mAppWidgetHost.allocateAppWidgetId();
		
		Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
		pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		
		// Adds a whole bunch of empty things
	    ArrayList<AppWidgetProviderInfo> customInfo = new ArrayList<AppWidgetProviderInfo>();
        pickIntent.putParcelableArrayListExtra( AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
        ArrayList<Bundle> customExtras = new ArrayList<Bundle>();
        
        pickIntent.putParcelableArrayListExtra( AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
		((Activity) c).startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);
	}
	
	public void configureWidget(Intent data, BoxHandlerModel model) {
	    Bundle extras = data.getExtras();
	    int appWidgetId2 = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
	    AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId2);
	    if (appWidgetInfo.configure != null) {
	        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
	        intent.setComponent(appWidgetInfo.configure);
	        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId2);
	        ((Activity) c).startActivityForResult(intent, REQUEST_ADD_APPWIDGET);
	    } else {
	        addWidget(data, model);
	    }
	}
	
	public void addWidget( Intent data, BoxHandlerModel model ) {
		Bundle extras = data.getExtras();
	    int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
	    AppWidgetProviderInfo appWidgetInfo =  mAppWidgetManager.getAppWidgetInfo(appWidgetId);
	    
		model.addBoxWidget(appWidgetInfo, appWidgetId, 1, 0, 200, 200, true);
	}
	
	public AppWidgetHost getHost() {
		return mAppWidgetHost;
	}
	
	public void deleteAppwidgetId() {
		mAppWidgetHost.deleteAppWidgetId(appWidgetId);
	}
}
