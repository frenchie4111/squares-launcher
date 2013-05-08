package org.mikelyons.squares;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.appwidget.AppWidgetHost;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BoxController implements Observer {
	private BoxHandlerModel model;
	private LinearLayout layout;
	private RelativeLayout overlay;
	private ArrayList<BoxButtonRow> rows;
	
	AppWidgetHost host;
	
	private Context c;
	
	public BoxController() {
		model = new BoxHandlerModel();
		setup();
	}
	
	public BoxController(LinearLayout layout, BoxHandlerModel model, AppWidgetHost host, Context c) {
		this.model = model;
		this.layout = layout;
		this.host = host;
		this.c = c;
		setup();
	}

	public void setContext(Context c) {
		this.c = c;
	}
	
	public void setOverlay( RelativeLayout overlay ){
		this.overlay = overlay;
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
				
				
				final BoxButton current = rows.get(i).getButtons().get(j);
				
				current.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						model.clickBox(row, index, c);
						v.setBackgroundColor(Color.YELLOW);
					}
				});
				
				current.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View arg0) {
						Log.v("BoxController", "Removing box: " + Integer.toString(row) + ": " + Integer.toString(index));
						model.removeBox(row, index);
						//beginDrag(current);
						return true;
					}
				});
				
			}
		}
	}

	public void beginDrag(final BoxButton b) {
		for( BoxButtonRow row : rows ) {
			row.removeView(b);
		}
		b.setOnTouchListener(new OnTouchListener() {
			int dx;
			int dy;
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
			    switch (motionEvent.getAction()) {
			        case MotionEvent.ACTION_DOWN:
			            dx = (int) motionEvent.getX();
			            dy = (int) motionEvent.getY();
			            break;

			        case MotionEvent.ACTION_MOVE:
			            int x = (int) motionEvent.getX();
			            int y = (int) motionEvent.getY();
			            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
			            int left = lp.leftMargin + (x - dx);
			            int top = lp.topMargin + (y - dy);
			            lp.leftMargin = left;
			            lp.topMargin = top;
			            view.setLayoutParams(lp);
			            break;
			            
			        case MotionEvent.ACTION_UP:
			        	endDrag(b);
			    }
			    return true;
			}
		});
		overlay.addView(b);
	}
	
	public void endDrag(BoxButton b) {
		b.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
			    return true;
			}
		});
	}
	
	public void addRow( BoxButtonRow row ) {
		this.rows.add(row);
		this.layout.addView(row);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		// TODO Only update rows that need it
		/*
		 * Need to loop through and only update where it seems like things have
		 * been changed. Because updating all of them is too slow
		 */
		for( int i = 0; i < rows.size() || i < model.getBoxRows().size(); i++ ) { 
			if( rows.size() <= i ) {
				// Should adda row if needed
				Log.v("Update", "Added a row for: " + i);
				this.rows.add(new BoxButtonRow(c));
			}
			Log.v("Updating row",Integer.toString(i));
			if( model.getBoxRows().get(i).needUpdate() ) { // Model knows if it needs update
				layout.removeView(rows.get(i));
				rows.get(i).update(model.getBoxRows().get(i), host);
				layout.addView(rows.get(i));
			}
		}
		setOnClickListeners();
	}
}
