package org.mikelyons.squares;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BoxButton extends RelativeLayout {
	
	// TODO Load these values from settings
	public static int BOX_WIDTH = 100;
	public static int BOX_HEIGHT = 100;
	
	public BoxButton(Context context) {
		super(context, null);
		setup();
	}
	
	public BoxButton(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		setup();
	}
	
	public BoxButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup();
	}
	
	public void setup() {
		// Parameters have to match parent layout type
		LinearLayout.LayoutParams new_params = new LinearLayout.LayoutParams(BOX_WIDTH,BOX_HEIGHT);
		
		new_params.setMargins(5, 5, 5, 5);
		
		setLayoutParams(new_params);
		
		RelativeLayout background = new RelativeLayout(this.getContext());
		background.setLayoutParams(new RelativeLayout.LayoutParams(BOX_WIDTH, BOX_HEIGHT));		
		background.setBackgroundColor(Color.GRAY);
		AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F);
		alpha.setDuration(0); // Make animation instant
		alpha.setFillAfter(true); // Tell it to persist after the animation ends
		// And then on your layout
		background.startAnimation(alpha);
		
		addView(background);
	}
	
	public void addIcon(Drawable icon) {
		ImageView new_icon = new ImageView(this.getContext());
		new_icon.setImageDrawable(icon);
		
		LayoutParams layoutParams = new LayoutParams(BOX_WIDTH/2, BOX_HEIGHT/2);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		new_icon.setLayoutParams(layoutParams);
		
		addView(new_icon);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}
	
	@Override
	protected void onMeasure(int width, int height) {
		super.onMeasure(width, height);
	}
}
