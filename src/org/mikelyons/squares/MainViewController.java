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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainViewController {	
	private BoxController bc;
	private LinearLayout layout;
	private RelativeLayout overlayContainer;
	private Context c;
	
	private Button fanButton;
	
	private ImageView drag_view;
	
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
	}
	
	public void beginDrag( ImageView button_to_drag ) {
		drag_view = button_to_drag;
	}
	
	public void continueDrag( int x, int y ) {
		// TODO Show preview
		if( drag_view != null ) { // Sometimes it's null, don't do anythign when it is
			overlayContainer.removeView(drag_view);
			
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) drag_view.getLayoutParams();
			lp.setMargins(x, y, 0, 0);
			drag_view.setLayoutParams(lp);
			
			overlayContainer.addView(drag_view);
			
			bc.showPreviewCoords(drag_view, x, y);
		}
	}
	
	public void endDrag( int x, int y ) {
		// TODO Make this add
		overlayContainer.removeView(drag_view);
	}
	
	public void addOverlay() {
		RelativeLayout rloverlay = new RelativeLayout(c);
		LinearLayout.LayoutParams overlaylp = 
				new LinearLayout.LayoutParams( 
						layout.getLayoutParams().width,
						layout.getLayoutParams().height );
		
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
