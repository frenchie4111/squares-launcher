package org.mikelyons.squares;

import java.util.ArrayList;
import java.util.Observable;

import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.util.Log;

public class BoxHandlerModel extends Observable {
	
	private ArrayList<BoxRowModel> boxRows;
	private SQLSettingsManager ssm;
	
	public BoxHandlerModel() {
		boxRows = new ArrayList<BoxRowModel>();
	}

	public ArrayList<BoxRowModel> getBoxRows() {
		return boxRows;
	}
	
	/**
	 * Adds a box with the given info to the given row
	 * 	The first row is referred to as row one
	 * @param row
	 * @param info
	 */
	public void addBox(ResolveInfo info, int row, int width, int height) {
		addBox( new ApplicationInfo( info ), row, 0, width, height, false );
	}
	
	/**
	 * Adds a box to the given index of the 
	 * @param info
	 * @param row
	 * @param index
	 */
	public void addBox(ResolveInfo info, int row, int index, int width, int height) {
		addBox( new ApplicationInfo(info), row, index, width, height, false);
	}
	
	public void addBox(ApplicationInfo info, int row, int index, int width, int height) {		
		addBox( info, row, index, width, height, false );
	}
	
	public void addBox(ApplicationInfo info, int row, int index, int width, int height, boolean save) {
		while( boxRows.size() < row ) {
			boxRows.add( new BoxRowModel() );
		}
		BoxModel new_box = boxRows.get(row - 1).addBox( info, index, width, height );
		
		if( save ) {
			ssm.updateBoxesAfterIncrement(row, index);
			ssm.addField(info.info.activityInfo.packageName, info.info.activityInfo.name , row, index, new_box.getWidth(), new_box.getHeight());
			ssm.printTable();
		}
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Adds a widget
	 * @param info
	 * @param row
	 * @param index
	 * @param widget
	 * @param height Whether or not to save it to the database
	 */
	public void addBoxWidget( AppWidgetProviderInfo info, int id, int row, int index, int width, int height, boolean save ) {
		while( boxRows.size() < row ) {
			boxRows.add( new BoxRowModel() );
		}
		// BoxModel new_box = boxRows.get(row - 1).addBox( info, index, width, height );
		BoxWidgetModel new_box = new BoxWidgetModel(info, id, width, height);
		boxRows.get(row - 1).addBox(new_box, index);
		
		if( save ) {
			ssm.updateBoxesAfterIncrement(row, index);
			ssm.addField( Integer.toString(id) , "n/a" , row, index, new_box.getWidth(), new_box.getHeight(), SQLSettingsHelper.TYPE_WIDGET);
			ssm.printTable();
		}
		
		setChanged();
		notifyObservers();
	}
	
	public void removeBox( int row, int index ) {
		if( this.boxRows.size() > row ) {
			Log.v("Removing Box", "" + Integer.toString(row));
			this.boxRows.get(row).removeBox(index);
			ssm.removeField(row+1, index);
			ssm.updateBoxesAfterDecrement(row+1, index);
			ssm.printTable();
		}
		
		setChanged();
		notifyObservers();
	}
	
	public void clickBox(int row, int index, Context c) {
		// TODO Error check to make sure box exists
		boxRows.get(row).getBoxes().get(index).start(c);
	}
	
	public void setSSM( SQLSettingsManager ssm ) {
		this.ssm = ssm;
	}
}
