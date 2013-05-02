package org.mikelyons.squares;

import com.deaux.fan.FanView;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainViewController {
	private BoxController bc;
	private LinearLayout layout;
	private RelativeLayout overlayContainer;
	private FanView fan;
	private Context c;
	
	
	public MainViewController() {
		bc = new BoxController();
	}
	
	public MainViewController( Context c, LinearLayout layout, FanView fan, BoxHandlerModel model ) {
		this.layout = layout;
		LinearLayout box_layout = (LinearLayout) layout.findViewById(R.id.boxViewContainer);
		bc = new BoxController( box_layout, model, c );
		this.fan = fan;
		this.c = c;
		overlayContainer = (RelativeLayout) layout.findViewById(R.id.mainViewRelativeContainer);
		addOverlay();
		bc.setOverlay(overlayContainer);
	}
	
	
	public void addOverlay() {
		RelativeLayout rloverlay = new RelativeLayout(c);
		LinearLayout.LayoutParams overlaylp = 
				new LinearLayout.LayoutParams( 
						layout.getLayoutParams().width,
						layout.getLayoutParams().height );
		rloverlay.setLayoutParams(overlaylp);
//		rloverlay.setBackgroundColor(Color.GREEN);
//		AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F);
//		alpha.setDuration(0); // Make animation instant
//		alpha.setFillAfter(true); // Tell it to persist after the animation ends
//		// And then on your layout
//		rloverlay.startAnimation(alpha);
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
				// TODO Auto-generated method stub
				fan.showMenu();
				if( fan.isOpen() ) {
					v.setBackgroundResource(R.drawable.hidefan2);
				} else {
					v.setBackgroundResource(R.drawable.showfan2);
				}
			}
		});
		rloverlay.addView(new_box);
		
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
	
	public RelativeLayout getOverlay() {
		return overlayContainer;
	}
}
