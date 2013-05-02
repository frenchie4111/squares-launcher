package org.mikelyons.squares;

import java.util.ArrayList;
import java.util.List;

import com.deaux.fan.FanView;

import android.os.Bundle;
import android.app.Activity;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	// Models
	BoxHandlerModel bhm;
	SQLSettingsManager ssm;
	
	// Views
	FanView fan;
	
	LinearLayout mainViewContainer;
	RelativeLayout mainViewRelativeContainer;
	LinearLayout fanViewContainer;
	
	// Controllers
	MainViewController mvc;
	FanViewController fvc;
	WidgetController wc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Side Menu Instantiation
		// NOTE: This goes first because you need to call setViews for the rest to work
		setContentView(R.layout.fan);
		fan = (FanView) findViewById(R.id.fan_wrapper);
		
		// TODO Load duration from preferences
		fan.setAnimationDuration(300);
		fan.setViews(R.layout.activity_main, R.layout.my_fan_view);
		
		// Get Views from XML
		mainViewContainer = (LinearLayout) findViewById(R.id.mainViewContainer);
		fanViewContainer = (LinearLayout) findViewById(R.id.fanViewContainer);
		
		mainViewRelativeContainer = (RelativeLayout) findViewById(R.id.mainViewRelativeContainer);
		
		// Instantiate Model
		bhm = new BoxHandlerModel();
		// TODO Add loading code
		
		// Load model from settings manager
		ssm = new SQLSettingsManager(this);
		ssm.open();
		//ssm.clearTable();
		bhm = ssm.getModel();
		
		// Add test values
		
		// Create intent and find info for omxpiremote
		final Intent new_intent = new Intent(Intent.ACTION_MAIN);
		new_intent.setComponent(new ComponentName("org.mikelyons.omxpiremote", "org.mikelyons.omxpiremote.MainActivity"));
		new_intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager pkg = this.getPackageManager();
		List<ResolveInfo> apps = pkg.queryIntentActivities(new_intent, 0);
		
		// End add test values
		
		// Instantiate Controllers. Should also draw model data to screen
		mvc = new MainViewController(this, mainViewContainer, fan, bhm);
		
		LinearLayout fanViewLinearLayout = (LinearLayout) findViewById(R.id.AllAppsLinear);
		fvc = new FanViewController(this, fanViewLinearLayout, fan, bhm);
		
		RelativeLayout overlay = mvc.getOverlay();
		
		wc = new WidgetController(this);
		wc.setLayout(overlay);
		
		Button button = new Button(this);
		button.setText("Choose Widget");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				wc.showDialog();
			}
		});
		overlay.addView(button);
	}

	
	@Override
	public void onResume() {
		super.onResume();
		// Wallpaper stuff
		// Get the manager
		WallpaperManager wpmgmt = WallpaperManager.getInstance(this);
		
		// Try to ask for the proper sized wallpaper
		Display display = getWindowManager().getDefaultDisplay();
		if( display.getOrientation() == Configuration.ORIENTATION_PORTRAIT ) {
			wpmgmt.suggestDesiredDimensions( display.getHeight(),
											 display.getWidth() );
		} else {
			wpmgmt.suggestDesiredDimensions( display.getWidth(),
					 						 display.getHeight() );
		}
		
		// Set the wall paper
		// TODO Used non-depreciated
		mainViewContainer.setBackgroundDrawable( wpmgmt.getDrawable() );
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ssm.close();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if( keyCode == KeyEvent.KEYCODE_MENU ) {
			fan.showMenu();
			return true;
		}
		if( keyCode == KeyEvent.KEYCODE_BACK ) {
			if( fan.isOpen() ){
				fan.showMenu();
				return true;
			}
			// TODO REMOVE THIS
			return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( resultCode == RESULT_OK ) {
			if( requestCode == WidgetController.REQUEST_PICK_APPWIDGET ) {
				Log.v("Widgetess", "Pick App Widget");
				wc.configureWidget(data);
			} else if( requestCode == WidgetController.REQUEST_ADD_APPWIDGET ) {
				Log.v("Widgetess", "Add App Widget");
				//wc.addWidget(data);
			}
		} else if (resultCode == RESULT_CANCELED && data != null) {
			int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
			if( appWidgetId != -1 ) {
				wc.deleteAppwidgetId();
			}
		}
	}

}
