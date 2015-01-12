package com.example.guild.mychecklist;

/**
 * Created by Guild on 12/22/2014.
 */
import java.io.File;
import java.util.LinkedList;
import java.util.List;

//import com.hmkcode.android.model.Book;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;


public class MyDatabase extends SQLiteAssetHelper{
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "checklistDB.sqlite";
    private Context context;

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/YOUR_PACKAGE/databases/";

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    // check table name
    private static final String TABLE_CHECK = "itemCheck";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_STATUS = "status";
    SQLiteQueryBuilder qb;

    private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_STATUS};

    public void addItem(String model, boolean st){
        Log.d("dbItem", model);
        Log.d("dbItem", ""+st);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, model); // get title

        if(st){
            values.put(KEY_STATUS, true);
        }else{
            values.put(KEY_STATUS, false);
        }

        db.insert(TABLE_CHECK, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        db.close();
    }

    public Cursor getItem(int id) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        qb.setTables(TABLE_CHECK);
        Cursor cursor =
                db.query(TABLE_CHECK, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();
        Log.d("getItem","get item : "+cursor.getString(0)+":"+cursor.getString(1)+":"+cursor.getString(2));
        return cursor;
    }
    public int getItemStatus(int id) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        qb.setTables(TABLE_CHECK);
        Cursor cursor =
                db.query(TABLE_CHECK, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        if(cursor.getInt(2) != 0){
            return 1 ;
        }else{
            return 0;
        }



    }

    public int checkTable(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_CHECK);
        Cursor cursor = qb.query(db, COLUMNS, null, null,
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        int count=0;
        while(!cursor.isAfterLast()){
            count++;
            cursor.moveToNext();
        }
        return count;

    }
    public void listItem(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_CHECK);
        Cursor cursor = qb.query(db, COLUMNS, null, null,
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()){
            Log.d("myApp","list item : "+cursor.getString(0)+":"+cursor.getString(1)+":"+cursor.getString(2));
            cursor.moveToNext();
        }


    }
    public int updateStatus(Checklist_Model model,int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("status", true);
        Log.d("dbItem", "status update check "+model.getCurrentCheck());
        int i = db.update(TABLE_CHECK, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(id) }); //selection args

        db.close();

        return i;

    }
    public int clearStatus(Checklist_Model model,int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("status", false);
        Log.d("dbItem", "status update check "+model.getCurrentCheck());
        int i = db.update(TABLE_CHECK, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(id) }); //selection args

        db.close();

        return i;

    }





}
