package org.mikelyons.squares;

import java.util.ArrayList;

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
	
	public void update(BoxRowModel model) {
		PackageManager pkg = this.getContext().getPackageManager();
		// TODO Optimize this more
		if( buttons.size() != model.getBoxes().size() ) {
			Log.v("Need update", "Need update");
			buttons.clear();
			this.removeAllViews();
			
			ArrayList<BoxModel> boxes = model.getBoxes();
			for( BoxModel box : boxes ) {
				Log.v("Added Box","Added box");
				addButton( box, box.getLabel(pkg), box.getIcon(pkg) );
			}
		}
	}

	public ArrayList<BoxButton> getButtons() {
		return buttons;
	}

}
