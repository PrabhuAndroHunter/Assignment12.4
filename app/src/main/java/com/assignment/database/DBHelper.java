package com.assignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.assignment.model.Employee;
import com.assignment.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 24/1/18.
 */

public class DBHelper {
    private SQLiteDatabase db;
    private final Context context;
    private final TablesClass dbHelper;
    public static int no;
    public static DBHelper db_helper = null;

    public static DBHelper getInstance(Context context) {
        try {
            if (db_helper == null) {
                db_helper = new DBHelper(context);
                db_helper.open();
            }
        } catch (IllegalStateException e) {
            //db_helper already open
        }
        return db_helper;
    }

    /*
     * set context of the class and initialize TableClass Object
	 */

    public DBHelper(Context c) {
        context = c;
        dbHelper = new TablesClass(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    /*
     * close databse.
     */
    public void close() {
        if (db.isOpen()) {
            db.close();
        }
    }

    public boolean dbOpenCheck() {
        try {
            return db.isOpen();
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * open database
     */
    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.v("open database Exception", "error==" + e.getMessage());
            db = dbHelper.getReadableDatabase();
        }
    }

    public long insertContentVals(String tableName, ContentValues content) {
        long id = 0;
        try {
            db.beginTransaction();
            id = db.insert(tableName, null, content);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    /*
     * Get count of all tables in a table as per the condition
	 */

    public int getFullCount(String table, String where) {
        Cursor cursor = db.query(false, table, null, where, null, null, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                no = cursor.getCount();
                cursor.close();
            }
        } finally {
            cursor.close();
        }
        return no;
    }

    public List <Employee> getEmployeeDetails() throws SQLException { // Creating method
        List <Employee> records = new ArrayList <Employee>();

        //Cursor exposes results from a query on a SQLiteDatabase.
        Cursor cursor = db.query(true, Constants.EMPLOYEE_TABLE, new String[]{Constants.ID, Constants.EMPLOYEE_NAME, Constants.EMPLOYEE_PHONE_NUMBER,
                Constants.EMPLOYEE_DATA_OF_BIRTH,}, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int employeeid = cursor.getInt(cursor.getColumnIndex(Constants.ID));
                String employeeName = cursor.getString(cursor.getColumnIndex(Constants.EMPLOYEE_NAME));
                String employeePhoneNumber = cursor.getString(cursor.getColumnIndex(Constants.EMPLOYEE_PHONE_NUMBER));
                String employeeDoB = cursor.getString(cursor.getColumnIndex(Constants.EMPLOYEE_DATA_OF_BIRTH));
                records.add(new Employee(employeeid, employeeName, employeeDoB, employeePhoneNumber));
            } while (cursor.moveToNext());
            cursor.close();
            return records;
        }
        cursor.close();
        return records; // Return statement
    }

}