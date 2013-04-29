package org.mikelyons.squares;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BoxButton extends RelativeLayout {
	
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
		LinearLayout.LayoutParams new_params = new LinearLayout.LayoutParams(200,200);
		
		new_params.setMargins(5, 5, 5, 5);
		
		setLayoutParams(new_params);
		setBackgroundColor(Color.RED);
	}
	
	public void addIcon(Drawable icon) {
		ImageView new_icon = new ImageView(this.getContext());
		new_icon.setImageDrawable(icon);
		
		LayoutParams layoutParams = new LayoutParams(100, 100);
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
