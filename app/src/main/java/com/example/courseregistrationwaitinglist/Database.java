package com.example.courseregistrationwaitinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "course_registration.db";
    public static final String TABLE_NAME = "STUDENT_LIST";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "COURSE";
    public static final String COL_3 = "PRIORITY";
    public static final String COL_4 = "SEMESTER";

    //To start the database
    public Database(Context ct) {
        super(ct, DATABASE_NAME, null, 1);
    }

    //To create the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COL_1 + " TEXT, " + COL_2 + " TEXT, " + COL_3 + " TEXT, " + COL_4 + " TEXT)");
    }

    //To update the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    //To insert new data into the database
    public boolean insertData(String name, String course, String priority, String semester) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, name);
        cv.put(COL_2, course);
        cv.put(COL_3, priority);
        cv.put(COL_4, semester);
        long result = db.insert(TABLE_NAME, null, cv);
        db.close();
        return result != -1;
    }

    public boolean updateData(String name, String course, String priority, String semester) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, name);
        cv.put(COL_2, course);
        cv.put(COL_3, priority);
        cv.put(COL_4, semester);
        long result = db.update(TABLE_NAME, cv, COL_1 + " = ?", new String[]{name});
        db.close();
        return result != -1;
    }

    public boolean deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, COL_1 + " = ?", new String[]{name});
        db.close();
        return result != -1;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " +
               COL_3 + " ASC", null);
    }
}
