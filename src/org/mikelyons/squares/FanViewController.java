package org.mikelyons.squares;

import java.util.HashMap;
import java.util.TreeSet;

import com.deaux.fan.FanView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FanViewController {
	
	Context c;
	
	LinearLayout fanView;
	FanView fan;
	LockableScrollView fanScroll;
	
	WidgetController wc;
	
	AllAppLoader mAppLoader;
	
	BoxHandlerModel model;
	
	MainViewController mvc;

	public FanViewController( Context c, LinearLayout fanView, FanView fan, WidgetController wc, BoxHandlerModel model, MainViewController mvc ) {
		this.c = c;
		this.fanView = fanView;
		this.fan = fan;
		this.model = model;
		this.wc = wc;
		this.mvc = mvc;
		
		this.fanScroll = (LockableScrollView) fan.findViewById(R.id.fanViewScrollContainer);
		
		populateList();
	}
	
	public void lockScroll() {
		this.fanScroll.setScrollingEnabled(false);
	}
	
	public void unlockScroll() {
		this.fanScroll.setScrollingEnabled(true);
	}
	
	private void populateList() {
		mAppLoader = new AllAppLoader( c, fanView, fan, wc, model );
	}
	
	public static void addBoxWithDrag( final Context c, final ApplicationInfo info, final BoxHandlerModel model, final int mx, final int my ) {		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				Log.v("Menu", "Menu Finsihed, adding box now");
				
				View new_view = new View(c);
				
				new_view.setLayoutParams( new RelativeLayout.LayoutParams(200, 200) );
				new_view.setBackgroundColor(Color.RED);
				
				((MainActivity) c).getMVC().beginDrag( new_view ); // Begin dragin in MVC (It's in mvc because it has the overlay)
			}	
		};
		((MainActivity) c).toggleMenu(r);
	}
	
	public static void addBoxWithDialog( Context c, final ApplicationInfo info, final BoxHandlerModel model ) {
		LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View layout = inflater.inflate(R.layout.new_icon_dialog, (ViewGroup) ((Activity) c).findViewById(R.id.newAppDialogRoot));
	    
	    TextView appLabel = (TextView) layout.findViewById(R.id.newAppDialogHeading);
	    appLabel.setText( info.getName( c.getPackageManager() ) );
	    appLabel.setHeight(100);
	    appLabel.setTextSize(20);
	    appLabel.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL );
	    
	    ImageView appIcon = (ImageView) layout.findViewById(R.id.newAppDialogImage);
	    appIcon.setImageDrawable(info.getIcon(c.getPackageManager()));
	    appIcon.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
	    
	    final NumberPicker npRow = (NumberPicker) layout.findViewById(R.id.npRow);
	    npRow.setMinValue(1);
	    npRow.setMaxValue(10);
	    
	    final NumberPicker npIndex = (NumberPicker) layout.findViewById(R.id.npIndex);
	    npIndex.setMinValue(1);
	    npIndex.setMaxValue(10);
	    
	    final EditText etWidth = (EditText) layout.findViewById(R.id.newAppDialogWidth);
	    etWidth.setText("200");
	    final EditText etHeight = (EditText) layout.findViewById(R.id.newAppDialogHeight);
	    etHeight.setText("200");
	    
	    AlertDialog.Builder builder = new AlertDialog.Builder(c).setView(layout);
	    
	    builder.setTitle("Adding App: " + info.getName(c.getPackageManager()) );
	    
	    AlertDialog alertDialog = builder.create();
	    
	    alertDialog.setButton(alertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
	    });
	    
	    alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "Add Icon", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				model.addBox(info, npRow.getValue(), npIndex.getValue()-1, Integer.parseInt(etWidth.getText().toString()), Integer.parseInt(etHeight.getText().toString()), true);
			}
	    });
	    
	    alertDialog.show();
		//model.addBox(info, 1, 0, 200, 150, true);
	}
	
	public void setBoxModel( BoxHandlerModel model ) {
		this.model = model;
	}
}
