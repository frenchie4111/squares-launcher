package org.mikelyons.squares;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class BoxController implements Observer {
	private BoxHandlerModel model;
	private LinearLayout layout;
	private ArrayList<BoxButtonRow> rows;
	
	private Context c;
	
	public BoxController() {
		model = new BoxHandlerModel();
		setup();
	}
	
	public BoxController(LinearLayout layout, BoxHandlerModel model, Context c) {
		this.model = model;
		this.layout = layout;
		this.c = c;
		setup();
	}

	public void setContext(Context c) {
		this.c = c;
	}
	
	public void setup() {
		PackageManager pkg = c.getPackageManager();
		
		rows = new ArrayList<BoxButtonRow>();
		
		ArrayList<BoxRowModel> rows = model.getBoxRows();
		for( BoxRowModel row : rows ) {			
			BoxButtonRow new_row = new BoxButtonRow(c);			
			this.rows.add(new_row);
		}
		
		update(new Observable(), new Object());
		setOnClickListeners();
		
		model.addObserver(this);
	}
	
	private void setOnClickListeners() {
		for( int i = 0 ; i < rows.size() ; i ++) {
			for( int j = 0; j < rows.get(i).getButtons().size(); j++ ) {
				final int row = i;
				final int index = j;
				
				BoxButton current = rows.get(i).getButtons().get(j);
				
				current.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						model.clickBox(row, index, c);
						v.setBackgroundColor(Color.YELLOW);
						Log.v("Clicked", "Trying to click: " + Integer.toString(row) + ", " + Integer.toString(index));
					}
				});
				
			}
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Only update rows that need it
		for( int i = 0; i < rows.size(); i++ ) {
			Log.v("Updating row",Integer.toString(i));
			layout.removeView(rows.get(i));
			rows.get(i).update(model.getBoxRows().get(i));
			layout.addView(rows.get(i));
		}
	}
}
