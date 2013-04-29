package org.mikelyons.squares;

import java.util.List;

import com.deaux.fan.FanView;

import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	// Models
	BoxHandlerModel bhm;
	
	// Views
	FanView fan;
	
	LinearLayout mainViewContainer;
	RelativeLayout mainViewRelativeContainer;
	LinearLayout fanViewContainer;
	
	// Controllers
	MainViewController mvc;
	FanViewController fvc;
	
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
		
		// Add test values
		
		// Create intent and find info for omxpiremote
		final Intent new_intent = new Intent(Intent.ACTION_MAIN);
		new_intent.setComponent(new ComponentName("org.mikelyons.omxpiremote", "org.mikelyons.omxpiremote.MainActivity"));
		new_intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager pkg = this.getPackageManager();
		List<ResolveInfo> apps = pkg.queryIntentActivities(new_intent, 0);
		
		Log.v("Apps size", Integer.toString(apps.size()));
		
		if( apps.size() == 1 ) {
			Log.v("Testing app", "Only one app");
			bhm.addBox(1, apps.get(0));
			bhm.addBox(1, apps.get(0));
			bhm.addBox(2, apps.get(0));
			bhm.addBox(3, apps.get(0));
			bhm.addBox(3, apps.get(0));
			bhm.addBox(3, apps.get(0));
			
			if( bhm.getBoxRows().get(0).getBoxes().get(0).getLabel(pkg).equals("OmxPiRemote") ) {
				Log.v("Testing app", "Creating the box succeeded");
			}
		}
		// End add test values
		
		// Instantiate Controllers. Should also draw model data to screen
		mvc = new MainViewController(mainViewContainer, bhm, this);
		
		LinearLayout fanViewLinearLayout = (LinearLayout) findViewById(R.id.AllAppsLinear);
		fvc = new FanViewController(this, fanViewLinearLayout, fan, bhm);
		
		// Test Overlay adding
		RelativeLayout rloverlay = new RelativeLayout(this);
		LinearLayout.LayoutParams overlaylp = 
				new LinearLayout.LayoutParams( 
						mainViewContainer.getLayoutParams().width,
						mainViewContainer.getLayoutParams().height );
		rloverlay.setLayoutParams(overlaylp);
		rloverlay.setBackgroundColor(Color.GREEN);
		AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F);
		alpha.setDuration(0); // Make animation instant
		alpha.setFillAfter(true); // Tell it to persist after the animation ends
		// And then on your layout
		rloverlay.startAnimation(alpha);
		mainViewRelativeContainer.addView(rloverlay);
		
		BoxButton new_box = new BoxButton(this);
		RelativeLayout.LayoutParams boxlp= new RelativeLayout.LayoutParams(new_box.getLayoutParams());
		boxlp.setMargins(200, 200, 0, 0);
		new_box.setLayoutParams(boxlp);
		rloverlay.addView(new_box);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if( keyCode == KeyEvent.KEYCODE_MENU ) {
			fan.showMenu();
			return true;
		}
		if( keyCode == KeyEvent.KEYCODE_BACK ) {
			//hideMenu();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
