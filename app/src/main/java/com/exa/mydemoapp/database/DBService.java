package com.exa.mydemoapp.database;

import java.lang.reflect.Field;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBService extends SQLiteOpenHelper {

	private final Context context;

	public DBService(Context context) {
		super(context, Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		/*Log.i(context.getPackageName(), "Creating database");
		try {
			executeRawFile(database, R.raw.database);
			Log.i(context.getPackageName(), "Inserting basic data");
			executeRawFile(database, R.raw.basic_data);	
			
//			onUpgrade(database, 1, App.DATABASE_VERSION);
		} catch (Exception e) {
			throw new RuntimeException("Fail to Create Database", e);
		}*/
	}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(DBService.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion);
		try {/*
			for (int i = oldVersion + 1; i <= newVersion; i++) {
				int resource = getVersionFile(i);
				if (resource == -1) {
					throw new RuntimeException(
							"Database upgrade missing upgrade file for version  "
									+ i);
				}
				Log.i("RxtabUpgrade", "Executing Database file version" + i);
				executeRawFile(db, resource);
			}
	*/	} catch (Exception ex) {
			throw new RuntimeException("Fail to Upgrade Database", ex);
		}
	}
}
