package org.mikelyons.squares;

import java.util.ArrayList;
import java.util.Observable;

import android.content.Context;
import android.content.pm.ResolveInfo;

public class BoxHandlerModel extends Observable {
	
	private ArrayList<BoxRowModel> boxRows;
	
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
	public void addBox(int row, ResolveInfo info) {
		// TODO On box add store it in the database
		while( boxRows.size() < row ) {
			boxRows.add( new BoxRowModel() );
		}
		boxRows.get(row - 1).addBox(info);
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Adds a box on the given row
	 * 
	 * NOTE: Dangerous/worthless to make a box without 
	 * any info
	 * @param row
	 */
	public void addBox(int row) {
		while( boxRows.size() < row ) {
			boxRows.add( new BoxRowModel() );
		}
		boxRows.get(row - 1).addBox();
	}
	
	public void clickBox(int row, int index, Context c) {
		// TODO Error check to make sure box exists
		boxRows.get(row).getBoxes().get(index).start(c);
	}
}
