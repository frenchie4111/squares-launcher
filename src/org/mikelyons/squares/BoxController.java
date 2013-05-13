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
	
	public void showPreviewCoords( ImageView icon, int x, int y ) {
		// TODO Write resolve code
		int temp_y = y; // Copy of y for calculation
		int row = 0;
		for( row = 0; row < rows.size() && temp_y > 0 ; row++ ) {
			temp_y -= rows.get(row).getHeight();
		}
		if( temp_y > 0 ) {
			row++;
		}
		
		Log.v("Resolved y to", "y: " + row);
		
		int temp_x = x;
		int index = 0;
		if( row < rows.size() ) { // Make sure we aren't adding a preview to the last row
			for( index = 0; index < rows.get(row-1).getButtons().size() && temp_x > 0; index++ ) {
				temp_x -= rows.get(row-1).getButtons().get(index).getWidth();
			}
		}
		if( temp_x > 0 ) {
			index++;
		}
		
		showPreview( icon, row-1, index-1 );
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
	
	@Override
	public void update(Observable observable, Object data) {
		/*
		 * Need to loop through and only update where it seems like things have
		 * been changed. Because updating all of them is too slow
		 */
		for( int i = 0; i < rows.size() || i < model.getBoxRows().size(); i++ ) { 
			if( rows.size() <= i ) {
				// Should add a row if needed
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
		setOnClickListeners();
	}
}
