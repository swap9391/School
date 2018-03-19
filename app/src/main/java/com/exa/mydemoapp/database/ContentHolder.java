package com.exa.mydemoapp.database;

import android.content.ContentValues;

public class ContentHolder {
	private String table;
	private String whereClause;
	private String[] whereArgs;
	private ContentValues values = new ContentValues();

	public String getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public String[] getWhereArgs() {
		return whereArgs;
	}

	public void setWhereArgs(String[] whereArgs) {
		this.whereArgs = whereArgs;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public ContentValues getValues() {
		return values;
	}

	public void setValues(ContentValues values) {
		this.values = values;
	}

}
