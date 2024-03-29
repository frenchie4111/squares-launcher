package org.mikelyons.squares;

import java.util.ArrayList;

import android.appwidget.AppWidgetHost;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class BoxButtonRow extends LinearLayout {
	private ArrayList<BoxButton> buttons;
	
	public BoxButtonRow(Context context) {
		super(context, null);
		setup();
	}
	
	public BoxButtonRow(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		setup();
	}
	
	public BoxButtonRow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup();
	}
	
	public void setup() {
		buttons = new ArrayList<BoxButton>();
		//setBackgroundColor(Color.BLUE);
	}
	
	public void addButton(BoxModel box, String label, Drawable icon) {
		BoxButton new_button = new BoxButton(box, this.getContext());
		new_button.addIcon(icon);
		buttons.add(new_button);
		addView(new_button);
	}
	
	public void addWidgetButton(BoxModel box, AppWidgetHost host) {
		BoxButtonWidget new_button = new BoxButtonWidget((BoxWidgetModel)box, this.getContext(), host);
		buttons.add(new_button);
		addView(new_button);
	}
	
	public void update(BoxRowModel model, AppWidgetHost host) {
		PackageManager pkg = this.getContext().getPackageManager();
		// TODO Optimize this more
		
		Log.v("Need update", "Need update");
		buttons.clear();
		this.removeAllViews();
		
		ArrayList<BoxModel> boxes = model.getBoxes();
		for( BoxModel box : boxes ) {
			Log.v("Added Box","Added box");
			if( box instanceof BoxWidgetModel ) {
				addWidgetButton(box, host);
			} else {
				addButton( box, box.getLabel(pkg), box.getIcon(pkg) );
			}
		}
		
		model.updated(); // Tell the model to no longer need an update
	}

	public ArrayList<BoxButton> getButtons() {
		return buttons;
	}

}
