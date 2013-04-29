package org.mikelyons.squares;

import android.content.Context;
import android.widget.LinearLayout;

public class MainViewController {
	private BoxController bc;
	private LinearLayout layout;
	
	
	public MainViewController() {
		bc = new BoxController();
	}
	
	public MainViewController( LinearLayout layout,  BoxHandlerModel model, Context c ) {
		this.layout = layout;
		LinearLayout box_layout = (LinearLayout) layout.findViewById(R.id.boxViewContainer);
		bc = new BoxController( box_layout, model, c );
	}
}
