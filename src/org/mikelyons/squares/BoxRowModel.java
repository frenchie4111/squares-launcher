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

	public BoxModel addBox(ApplicationInfo info, int index, int width, int height) {
		this.boxes.add(index, new BoxModel(info, width, height));
		return this.boxes.get(index);
	}
	
	public void addBox(ResolveInfo info, int index) {
		this.boxes.add(index, new BoxModel(info));
	}
	
	/**
	 * Adds a box to the end of the row with the given info
	 * @param info
	 */
	public void addBox(ResolveInfo info) {
		addBox( info, this.boxes.size() );
	}
	
	public void addBox( BoxModel box, int index ) {
		this.boxes.add(index, box);
	}
	
	public void removeBox(int index) {
		if( boxes.size() > index ) {
			this.boxes.remove(index);
		}
	}
	
	/**
	 * Adds a box to the row
	 */
	public void addBox() {
		boxes.add(new BoxModel());
	}
	
}
