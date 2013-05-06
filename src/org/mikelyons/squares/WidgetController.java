package org.mikelyons.squares;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
	        //addWidget(data, model);
	    	addWidgetWithDialog( c, appWidgetInfo, appWidgetId2, model );
	    }
	}
	
	public static void addWidgetWithDialog( Context c, final AppWidgetProviderInfo info, final int appWidgetId, final BoxHandlerModel model ) {
		LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View layout = inflater.inflate(R.layout.new_icon_dialog, (ViewGroup) ((Activity) c).findViewById(R.id.newAppDialogRoot));
	    
	    final NumberPicker npRow = (NumberPicker) layout.findViewById(R.id.npRow);
	    npRow.setMinValue(1);
	    npRow.setMaxValue(10);
	    
	    final NumberPicker npIndex = (NumberPicker) layout.findViewById(R.id.npIndex);
	    npIndex.setMinValue(1);
	    npIndex.setMaxValue(10);
	    
	    final EditText etWidth = (EditText) layout.findViewById(R.id.newAppDialogWidth);
	    etWidth.setText( Integer.toString(info.minWidth) );
	    final EditText etHeight = (EditText) layout.findViewById(R.id.newAppDialogHeight);
	    etHeight.setText( Integer.toString(info.minHeight) );
	    
	    AlertDialog.Builder builder = new AlertDialog.Builder(c).setView(layout);
	    
	    builder.setTitle("Adding Widget: " + info.label );
	    
	    AlertDialog alertDialog = builder.create();
	    
	    alertDialog.setButton(alertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
	    });
	    
	    alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "Add Widget", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.v("Add Box With Dialog", "Accepted");
				model.addBoxWidget(info, appWidgetId, npRow.getValue(), npIndex.getValue()-1, Integer.parseInt(etWidth.getText().toString()), Integer.parseInt(etHeight.getText().toString()), true);
			}
	    });
	    
	    alertDialog.show();
		//model.addBox(info, 1, 0, 200, 150, true);
	}
	
	public void addWidget( Context c, Intent data, BoxHandlerModel model ) {
		Bundle extras = data.getExtras();
	    int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
	    AppWidgetProviderInfo appWidgetInfo =  mAppWidgetManager.getAppWidgetInfo(appWidgetId);
	    
		//model.addBoxWidget(appWidgetInfo, appWidgetId, 1, 0, 200, 200, true);
	    addWidgetWithDialog(c, appWidgetInfo, appWidgetId, model);
	}
	
	public AppWidgetHost getHost() {
		return mAppWidgetHost;
	}
	
	public void deleteAppwidgetId() {
		mAppWidgetHost.deleteAppWidgetId(appWidgetId);
	}
}
