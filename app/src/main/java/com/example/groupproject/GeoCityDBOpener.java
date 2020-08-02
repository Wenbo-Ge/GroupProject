package com.example.groupproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GeoCityDBOpener extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "GeoCityDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "FAVCITIES";
    public final static String COL_COUNTRY = "COUNTRY";
    public final static String COL_REGION = "REGION";
    public final static String COL_CITY = "CITY";
    public final static String COL_LATITUDE = "LATITUDE";
    public final static String COL_LONGITUDE = "LONGITUDE";
    public final static String COL_CURRENCY = "CURRENCY";
    public final static String COL_ID = "_id";

    public GeoCityDBOpener(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    };

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_COUNTRY + " text,"
                + COL_REGION + " text,"
                + COL_CITY + " text,"
                + COL_LATITUDE + " text,"
                + COL_LONGITUDE + " text,"
                + COL_CURRENCY + " text);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }
}
