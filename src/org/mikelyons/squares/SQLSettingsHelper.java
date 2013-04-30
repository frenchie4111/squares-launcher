package org.mikelyons.squares;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLSettingsHelper extends SQLiteOpenHelper {

	public static String DATABASE_NAME = "Squares.db";
	public static int DATABASE_VERSION = 2;
	public static String TABLE_NAME = "launcheritems";
	public static String ROW_NAME_PACKAGE = "package";
	public static String ROW_NAME_ACTIVITY = "activity";
	public static String ROW_NAME_ROW_NUM = "rownum";
	public static String ROW_NAME_INDEX = "rowindex";
	public static String[] ALL_COLUMNS = {ROW_NAME_PACKAGE,
										  ROW_NAME_ACTIVITY,
										  ROW_NAME_ROW_NUM, 
										  ROW_NAME_INDEX};
	
	private static String create_query = "CREATE TABLE " + TABLE_NAME + "(" + 
			ROW_NAME_PACKAGE + " TEXT, " + 
			ROW_NAME_ACTIVITY + " TEXT, " +
			ROW_NAME_ROW_NUM + " INT(10), " + 
			ROW_NAME_INDEX + " INT(10));";
	
	public SQLSettingsHelper( Context c ) {
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub		
		db.execSQL(create_query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// For right now we are just going to drop the table and recreate it when upgraded
		db.execSQL("DROP TABLE " + TABLE_NAME);
		db.execSQL(create_query);
	}
}