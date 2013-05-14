package org.mikelyons.squares;

import java.util.ArrayList;

import android.content.pm.ResolveInfo;

public class BoxRowModel {
	private ArrayList<BoxModel> boxes;
	
	private boolean needUpdate;
	
	public BoxRowModel() {
		boxes = new ArrayList<BoxModel>();
		needUpdate = true;
	}

	public ArrayList<BoxModel> getBoxes() {
		return boxes;
	}

	public BoxModel addBox(ApplicationInfo info, int index, int width, int height) {
		if( index <= boxes.size() ) {
			this.boxes.add(index, new BoxModel(info, width, height));
			needUpdate = true;
			return this.boxes.get(index);
		}
		return null;
	}
	
	public void addBox(ResolveInfo info, int index) {
		this.boxes.add(index, new BoxModel(info));
		needUpdate = true;
	}
	
	/**
	 * Adds a box to the end of the row with the given info
	 * @param info
	 */
	public void addBox(ResolveInfo info) {
		addBox( info, this.boxes.size() );
		needUpdate = true;
	}
	
	public void addBox( BoxModel box, int index ) {
		if( index <= boxes.size() ) {
			this.boxes.add(index, box);
			needUpdate = true;
		}
	}
	
	public void removeBox(int index) {
		if( boxes.size() > index ) {
			this.boxes.remove(index);
		}
		needUpdate = true;
	}
	
	/**
	 * Adds a box to the row
	 */
	public void addBox() {
		boxes.add(new BoxModel());
		needUpdate = true;
	}
	
	public boolean needUpdate() {
		return needUpdate;
	}
	
	public void setChanged() {
		this.needUpdate = true;
	}
	
	public void updated() {
		this.needUpdate = false;
	}
}
