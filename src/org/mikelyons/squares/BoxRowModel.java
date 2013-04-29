package org.mikelyons.squares;

import java.util.ArrayList;

import android.content.pm.ResolveInfo;

public class BoxRowModel {
	private ArrayList<BoxModel> boxes;
	
	public BoxRowModel() {
		boxes = new ArrayList<BoxModel>();
	}

	public ArrayList<BoxModel> getBoxes() {
		return boxes;
	}

	/**
	 * Adds a box to the end of the row with the given info
	 * @param info
	 */
	public void addBox(ResolveInfo info) {
		boxes.add(new BoxModel(info));
	}
	
	/**
	 * Adds a box to the row
	 */
	public void addBox() {
		boxes.add(new BoxModel());
	}
	
}
