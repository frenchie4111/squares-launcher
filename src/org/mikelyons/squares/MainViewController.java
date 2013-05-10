package org.mikelyons.squares;

import com.deaux.fan.FanView;

import android.appwidget.AppWidgetHost;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.view.View.OnDragListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainViewController {
	private BoxController bc;
	private LinearLayout layout;
	private RelativeLayout overlayContainer;
	private Context c;
	
	private Button fanButton;
	
	
	public MainViewController() {
		bc = new BoxController();
	}
	
	public MainViewController( Context c, LinearLayout layout, FanView fan, AppWidgetHost host, BoxHandlerModel model ) {
		this.layout = layout;
		LinearLayout box_layout = (LinearLayout) layout.findViewById(R.id.boxViewContainer);
		bc = new BoxController( box_layout, model, host, c );
		this.c = c;
		overlayContainer = (RelativeLayout) layout.findViewById(R.id.mainViewRelativeContainer);
		addOverlay();
		bc.setOverlay(overlayContainer);
	}
	
	public void beginDrag( View button_to_drag, int x, int y ) {
		
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) button_to_drag.getLayoutParams();
		
		lp.setMargins(x, y, 0, 0);
		
		button_to_drag.setLayoutParams(lp);
		
		// Add touch listener
		button_to_drag.setOnTouchListener( new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.v("Begin Drag", "Item Touched");
				ClipData data = ClipData.newPlainText("data", "asdf");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
				v.startDrag(data, shadowBuilder, v, 0);
				switch( event.getAction() ) {
					case MotionEvent.ACTION_DOWN:
						Log.v("Touched", "Touched");
				}
				return true;
			}
		});
		
		// Obtain MotionEvent object
//		long downTime = SystemClock.uptimeMillis();
//		long eventTime = SystemClock.uptimeMillis() + 100;
//		float tx = (float) x;
//		float ty = (float) y;
//		// List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
//		int metaState = 0;
//		MotionEvent motionEvent = MotionEvent.obtain(
//		    downTime, 
//		    eventTime, 
//		    DragEvent.ACTION_DRAG_STARTED, 
//		    tx, 
//		    ty, 
//		    metaState
//		);
		

		// Dispatch touch event to view
		//button_to_drag.dispatchTouchEvent(motionEvent);
		
		overlayContainer.addView(button_to_drag);
		
		//motionEvent.recycle();
	}
	
	public void addOverlay() {
		RelativeLayout rloverlay = new RelativeLayout(c);
		LinearLayout.LayoutParams overlaylp = 
				new LinearLayout.LayoutParams( 
						layout.getLayoutParams().width,
						layout.getLayoutParams().height );
		rloverlay.setClickable(true);
		rloverlay.setOnHoverListener( new OnHoverListener() {

			@Override
			public boolean onHover(View v, MotionEvent event) {
				Log.v("Hovering on overlay","Hovering on overlay");
				return true;
			}
			
		});
		
		rloverlay.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v("Overlay","Clicked");
				
			}
		});
		
		rloverlay.setOnDragListener(new OnDragListener() {
			@Override
			public boolean onDrag(View v, DragEvent event) {
				Log.v("Overlay","Dragging over overlay");
				switch( event.getAction() ) {
					case DragEvent.ACTION_DROP:
						Log.v("Overlay", "Dropped on overlay");
						break;
						
					case DragEvent.ACTION_DRAG_ENTERED:
						break;
				}
				return false;
			}
		});
		
		rloverlay.setLayoutParams(overlaylp);

		overlayContainer.addView(rloverlay);	
		Button new_box = new Button(c);
		RelativeLayout.LayoutParams boxlp = new RelativeLayout.LayoutParams(75,75);
		boxlp.setMargins(20, 20, 20, 20);
		boxlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		boxlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		new_box.setLayoutParams(boxlp);
		new_box.setBackgroundResource(R.drawable.showfan2);
		new_box.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Runnable r = new Runnable() {
					@Override
					public void run() {
						Log.v("Menu", "Menu Finished");
					}
				};
				((MainActivity) c).toggleMenu(r);
			}
		});
		rloverlay.addView(new_box);
		
		fanButton = new_box;
		
		// Widget Testing Stuff
		
		
//		BoxButton drag_box = new BoxButton(c);
//		drag_box.setBackgroundColor(Color.MAGENTA);
//		drag_box.setOnTouchListener(new OnTouchListener() {
//			int dx;
//			int dy;
//			boolean first = true;
//			@Override
//			public boolean onTouch(View view, MotionEvent motionEvent) {
//			    switch (motionEvent.getAction()) {
//			        case MotionEvent.ACTION_DOWN:
//			            dx = (int) motionEvent.getX();
//			            dy = (int) motionEvent.getY();
//			            break;
//
//			        case MotionEvent.ACTION_MOVE:
//			        	if( first ) {
//			        		dx = (int) motionEvent.getX();
//				            dy = (int) motionEvent.getY();
//			        		first = false;
//			        	}
//			            int x = (int) motionEvent.getX();
//			            int y = (int) motionEvent.getY();
//			            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
//			            int left = lp.leftMargin + (x - dx);
//			            int top = lp.topMargin + (y - dy);
//			            lp.leftMargin = left;
//			            lp.topMargin = top;
//			            view.setLayoutParams(lp);
//			            break;
//			    }
//			    return true;
//			}
//		});
//		rloverlay.addView(drag_box);
	}
	
	public Button getFanButton() {
		return fanButton;
	}
	
	public RelativeLayout getOverlay() {
		return overlayContainer;
	}
}
