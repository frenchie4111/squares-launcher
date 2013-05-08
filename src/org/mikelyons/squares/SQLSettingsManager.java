package org.mikelyons.squares;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLSettingsManager {

	SQLiteDatabase database;
	SQLSettingsHelper helper;
	Context c;
	
	/**
	 * Creates a new instance of the sqlsettingsmanager with the given context
	 * @param context The context to open the database in
	 */
	public SQLSettingsManager(Context context) {
	    helper = new SQLSettingsHelper(context);
	    c = context;
	}
	
	/**
	 * Opens the database
	 * Be sure to close it!
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = helper.getWritableDatabase();
		Log.v("SQLSettingsManager", "Opened");
	}
	
	/**
	 * Closes the database
	 */
	public void close() {
		database.close();
	}
	
	/**
	 * Adds the given field to the table
	 * @param pkg Package name
	 * @param act Activity Name (Normally Package name + something)
	 * @param row The row the button is on
	 * @param index The index the button is on
	 */
	public void addField(String pkg, String act, int row, int index, int width, int height) {
		ContentValues cv = new ContentValues();
		cv.put(SQLSettingsHelper.ROW_NAME_PACKAGE, pkg);
		cv.put(SQLSettingsHelper.ROW_NAME_ACTIVITY, act);
		cv.put(SQLSettingsHelper.ROW_NAME_ROW_NUM, row);
		cv.put(SQLSettingsHelper.ROW_NAME_INDEX, index);
		cv.put(SQLSettingsHelper.ROW_NAME_WIDTH, width);
		cv.put(SQLSettingsHelper.ROW_NAME_HEIGHT, height);
		
		long returnValue = database.insert( SQLSettingsHelper.TABLE_NAME, null, cv );
		
		Log.v("SQLSettingsManager", "Returned: " + Long.toString(returnValue));
	}
	
	public void addField(String pkg, String act, int row, int index, int width, int height, String type) {
		ContentValues cv = new ContentValues();
		cv.put(SQLSettingsHelper.ROW_NAME_PACKAGE, pkg);
		cv.put(SQLSettingsHelper.ROW_NAME_ACTIVITY, act);
		cv.put(SQLSettingsHelper.ROW_NAME_ROW_NUM, row);
		cv.put(SQLSettingsHelper.ROW_NAME_INDEX, index);
		cv.put(SQLSettingsHelper.ROW_NAME_WIDTH, width);
		cv.put(SQLSettingsHelper.ROW_NAME_HEIGHT, height);
		cv.put(SQLSettingsHelper.ROW_NAME_TYPE, type);
		
		long returnValue = database.insert( SQLSettingsHelper.TABLE_NAME, null, cv );
		
		Log.v("SQLSettingsManager", "Returned: " + Long.toString(returnValue));
	}
	
	/**
	 * Removes the field from the table
	 * @param row Row of the button to remove the field for
	 * @param index Index of the button to remove the field for
	 */
	public void removeField( int row, int index ) {
		String where = SQLSettingsHelper.ROW_NAME_ROW_NUM + " = ?"
				+ " AND " + SQLSettingsHelper.ROW_NAME_INDEX + " = ?";
		String[] whereArgs = {Integer.toString(row), Integer.toString(index)};
		database.delete(SQLSettingsHelper.TABLE_NAME, where, whereArgs);
	}
	
	/**
	 * Increments the boxes index of all of the boxes after the given one
	 * CALL THIS BEFORE ADDING VALUES TO DB
	 * @param row
	 * @param index
	 */
	public void updateBoxesAfterIncrement(int row, int index) {
		String query = "UPDATE " + SQLSettingsHelper.TABLE_NAME + 
						" SET " + SQLSettingsHelper.ROW_NAME_INDEX + " = " +
						SQLSettingsHelper.ROW_NAME_INDEX + "+1 WHERE " + 
						SQLSettingsHelper.ROW_NAME_ROW_NUM + " = " + row + " AND " +
						SQLSettingsHelper.ROW_NAME_INDEX + " >= " + index;
		Log.v("SQLSettingsManager", query);
		database.execSQL(query);
	}
	
	/**
	 * Decrements the boxes index of all the boxes after and at the given index
	 * CALL THIS AFTER REMOVING A VALUE TO UPDATE ALL OF THE BOXES AFTER IT
	 * @param row The row of the box
	 * @param index The index to decrement at and everything after
	 */
	public void updateBoxesAfterDecrement(int row, int index) {
		String query = "UPDATE " + SQLSettingsHelper.TABLE_NAME + 
				" SET " + SQLSettingsHelper.ROW_NAME_INDEX + " = " +
				SQLSettingsHelper.ROW_NAME_INDEX + "-1 WHERE " + 
				SQLSettingsHelper.ROW_NAME_ROW_NUM + " = " + row + " AND " +
				SQLSettingsHelper.ROW_NAME_INDEX + " >= " + index;
		Log.v("SQLSettingsManager", query);
		database.execSQL(query);
	}
	
	/**
	 * Clears the table (For now it just calls onUpgrade which drops/recreates the table)
	 */
	public void clearTable() {
		helper.onUpgrade(database, 0, 0);
	}
	
	/**
	 * Prints the table through log for debugging
	 */
	public void printTable() {		
		Cursor cursor = database.query(SQLSettingsHelper.TABLE_NAME,
				SQLSettingsHelper.ALL_COLUMNS, null, null, null, null, SQLSettingsHelper.ROW_NAME_INDEX + " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Log.v("SQLSettingsManager", "Package: " + cursor.getString(0));
			Log.v("SQLSettingsManager", "Activity: " + cursor.getString(1));
			Log.v("SQLSettingsManager", "Row: " + Integer.toString(cursor.getInt(2)));
			Log.v("SQLSettingsManager", "Index: " + Integer.toString(cursor.getInt(3)));
			Log.v("SQLSettingsManager", "Width: " + Integer.toString(cursor.getInt(4)));
			Log.v("SQLSettingsManager", "Height: " + Integer.toString(cursor.getInt(5)));
			Log.v("SQLSettingsManager", "Type: " + cursor.getString(6));
			
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
	}
	
	public BoxHandlerModel getModel() {
		BoxHandlerModel new_model = new BoxHandlerModel();
		
		AppWidgetManager awm = AppWidgetManager.getInstance(c);
		
		Cursor cursor = database.query(SQLSettingsHelper.TABLE_NAME,
				SQLSettingsHelper.ALL_COLUMNS, null, null, null, null, SQLSettingsHelper.ROW_NAME_INDEX + " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Log.v("SQLSettingsManager", "Package: " + cursor.getString(0));
			Log.v("SQLSettingsManager", "Activity: " + cursor.getString(1));
			Log.v("SQLSettingsManager", "Row: " + Integer.toString(cursor.getInt(2)));
			Log.v("SQLSettingsManager", "Index: " + Integer.toString(cursor.getInt(3)));
			Log.v("SQLSettingsManager", "Width: " + Integer.toString(cursor.getInt(4)));
			Log.v("SQLSettingsManager", "Height: " + Integer.toString(cursor.getInt(5)));
			Log.v("SQLSettingsManager", "Type: " + cursor.getString(6));
			
			if( cursor.getString(6).equals(SQLSettingsHelper.TYPE_ICON) ) {
				
				ApplicationInfo info = new ApplicationInfo(cursor.getString(0), cursor.getString(1), c.getPackageManager());
				new_model.addBox(info, cursor.getInt(2), cursor.getInt(3),cursor.getInt(4),cursor.getInt(5));
				
			} else if (cursor.getString(6).equals(SQLSettingsHelper.TYPE_WIDGET)) {
				
				int appWidgetId = Integer.parseInt(cursor.getString(0));
				AppWidgetProviderInfo info = awm.getAppWidgetInfo(appWidgetId);
				
				new_model.addBoxWidget(info, appWidgetId, cursor.getInt(2), cursor.getInt(3),cursor.getInt(4),cursor.getInt(5), false);
			}
			
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		
		new_model.setSSM(this);
		
		return new_model;
	}
}
