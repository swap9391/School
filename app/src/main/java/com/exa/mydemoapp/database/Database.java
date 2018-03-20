package com.exa.mydemoapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Swapnil on 30/01/2016.
 */


public class Database extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "SchoolDb";
    public static final int DATABASE_VERSION = 1;

    public static final String STUDENT_DATA = "Create table if not exists student_data"
            + " ( localId integer primary key autoincrement," +
            "_id integer," +
            "registrationDate VARCHAR DEFAULT NULL," +
            "schoolName VARCHAR DEFAULT NULL," +
            "className VARCHAR DEFAULT NULL," +
            "divisionId VARCHAR DEFAULT NULL," +
            "registrationId VARCHAR DEFAULT NULL," +
            "studentName VARCHAR DEFAULT NULL," +
            "studentAddress VARCHAR DEFAULT NULL," +
            "studentUserName VARCHAR DEFAULT NULL," +
            "studentPassword VARCHAR DEFAULT NULL," +
            "userType VARCHAR DEFAULT NULL," +
            "studentBloodGrp VARCHAR DEFAULT NULL," +
            "gender VARCHAR DEFAULT NULL," +
            "totalFees VARCHAR DEFAULT NULL," +
            "installmentType VARCHAR DEFAULT NULL," +
            "installment1 VARCHAR DEFAULT NULL," +
            "installment2 VARCHAR DEFAULT NULL," +
            "installment3 VARCHAR DEFAULT NULL," +
            "rollNumber VARCHAR DEFAULT NULL," +
            "contactNumber VARCHAR DEFAULT NULL," +
            "dateInsvestment2 VARCHAR DEFAULT NULL," +
            "dateInsvestment3 VARCHAR DEFAULT NULL," +
            "subscribed VARCHAR DEFAULT NULL," +
            "visiblity VARCHAR DEFAULT NULL)";

    public static final String LOGIN_DATA = "Create table if not exists login_data"
            + " ( localId integer primary key autoincrement," +
            "_id integer," +
            "username VARCHAR DEFAULT NULL," +
            "password VARCHAR DEFAULT NULL)";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = context.openOrCreateDatabase(
                DATABASE_NAME, context.MODE_PRIVATE, null);

        db.execSQL(STUDENT_DATA);
        db.execSQL(LOGIN_DATA);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(STUDENT_DATA);
        db.execSQL(LOGIN_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        String studentTbl = "DEOP TABLE IF EXIST class_data";
        db.execSQL(studentTbl);
        onCreate(db);
    }


    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }


    }

}

