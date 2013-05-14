package org.mikelyons.squares;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.appwidget.AppWidgetHost;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BoxController implements Observer {
	
	public static int DEFAULT_WIDTH = 100;
	public static int DEFAULT_HEIGHT = 100;
	
	private BoxHandlerModel model;
	private LinearLayout layout;
	private ArrayList<BoxButtonRow> rows;
	
	AppWidgetHost host;
	
	private Context c;
	
	private BoxButton preview_view; // Variables for drag/drop preview showing
	private int preview_row = -1, preview_index = -1;
	private boolean show_preview = false, preview_changed = false;
	private BoxButtonRow preview_temp_row; // A row on the bottom that is for previews
	
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
	
	public void setup() {
		PackageManager pkg = c.getPackageManager();
		
		rows = new ArrayList<BoxButtonRow>();
		
		ArrayList<BoxRowModel> rows = model.getBoxRows();
		for( BoxRowModel row : rows ) {			
			BoxButtonRow new_row = new BoxButtonRow(c);			
			this.rows.add(new_row);
		}
		
		preview_temp_row = new BoxButtonRow(c);
		
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
						return true;
					}
				});
				
			}
		}
	}
	
	public void addRow( BoxButtonRow row ) {
		this.rows.add(row);
		this.layout.addView(row);
	}
	
	public Point resolveCoords( int x, int y) {
		
		int temp_y = y;
		int row = 0;
		for( row = 0; row < rows.size() && temp_y > 0; row++ ) {
			temp_y -= rows.get(row).getHeight();
			Log.v("Resolving", "Subtracted. Temp_y: " + temp_y + " row: " + row);
		}
		if( temp_y > 0 ) { // If it didn't all get removed, that means we are farther down
			row++; // Add one so we are below the bottom row
		}
		if( row < 1 ) { // Never less than 1 (If the person is clicking too high up)
			row = 1;
		}
		Log.v("Resolving", "found row row: " + row);
		
		int temp_x = x;
		int index = 1;
		if( row-1 < rows.size() ) {
			Log.v("Resolving","Row in range");
			for( index = 0; index < rows.get(row-1).getButtons().size() && temp_x > 0; index++) {
				temp_x -= rows.get(row-1).getButtons().get(index).getWidth();
				Log.v("Resolving", "Subtracted. Temp_x: " + temp_x + " index: " + index);
			}
			if( temp_x > 0 ) {
				index++;
			}
		}
		Log.v("Resolving", "found index index: " + index);
		
		return new Point(row-1, index-1);
	}
	
	public void showPreviewCoords( ImageView icon, int x, int y ) {
		// TODO Write resolve code
		Point resolved = resolveCoords(x,y);
		showPreview( icon, resolved.x, resolved.y );
	}
	
	public void showPreview( ImageView icon, int row, int index ) {
		if( preview_view == null ) {
			preview_view = new BoxButton(c);
		}
		if( previewChanged(row, index) ) {
			if( show_preview ) { // If it was already showing a preview
				Log.v("Preview", "Removing the old view");
				if( preview_row < rows.size() ) { // Make sure it wasn't the last row
					this.rows.get(preview_row).removePreview(preview_view);
				}
				// Then remove the preview from it's old view
			}
			preview_view = new BoxButton(c); // (Reset so icon resets)
			preview_view.setColor(Color.RED);
			preview_view.addIcon(icon.getDrawable());
			preview_row = row;
			preview_index = index;
			show_preview = true;
			preview_changed = true;
		
			update( new Observable(), new Object() );
		}
	}
	
	public boolean previewChanged( int row, int index ) {
		if( row != preview_row || index != preview_index ) {
			return true;
		}
		return false;
	}
	
	public void endPreview() {
		this.show_preview = false;
		this.preview_changed = false;
		
		this.preview_temp_row.removeAllViews();
		this.layout.removeView(preview_temp_row);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		/*
		 * Need to loop through and only update where it seems like things have
		 * been changed. Because updating all of them is too slow
		 */
		Log.v("BC Update","Rows size: " + rows.size());
		this.preview_temp_row.removeAllViews();
		this.layout.removeView(preview_temp_row);
		
		while( rows.size() > model.getBoxRows().size() ) {
			Log.v("BC Update", "Removing a row");
			layout.removeViewAt(rows.size() - 1);
			rows.remove(rows.size() - 1); // Remove the last if it's bigger
		}
		
		for( int i = 0; i < rows.size() || i < model.getBoxRows().size(); i++ ) { 
			if( rows.size() <= i ) {
				// Should add a row if needed
				Log.v("BC Update", "Adding row");
				this.rows.add(new BoxButtonRow(c));
			}
			
			// Add current row to the layout if it isn't already
			if( rows.get(i).getParent() == null ) {
				layout.addView(rows.get(i));
			}
			
			if( model.getBoxRows().get(i).needUpdate() ) { // Model knows if it needs update
				rows.get(i).update(model.getBoxRows().get(i), host);
			}
			
			if( show_preview && preview_changed ) {
				if( i == preview_row ) {
					Log.v("BC", "Adding Preview");
					rows.get(i).removePreview( preview_view );
					rows.get(i).addPreview(preview_view, preview_index);
					preview_changed = false;
				}
			}
		}
		if( show_preview && preview_changed ) { // If no preview added, that means it needs a row
			preview_temp_row.removePreview(preview_view);
			preview_temp_row.addPreview(preview_view, 0);
			this.layout.addView( preview_temp_row );
		}
		setOnClickListeners();
	}
}
