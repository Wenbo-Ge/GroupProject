package com.example.groupproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {


    protected final static String DATABASE_NAME = "SongsDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "FAVOUR_SONG";
    public final static String COL_TITLE = "TITLE";
    public final static String COL_DURATION = "DURATION";
    public final static String COL_ALBUMNAME = "ALBUMNAME";
    public final static String COL_ALBUMCOVER = "ALBUMCOVER";
    public final static String COL_ID = "_id";
    public final static String COL_LINK = "LINK";
    public final static String COL_FAVOURITE = "ISFAVOUTITE";

    public MyDatabaseOpenHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TITLE text," + "DURATION text," + "ALBUMNAME text,"+"ALBUMCOVER text," + "LINK text," + "ISFAVOUTITE text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //Create the new table:
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //Create the new table:
        onCreate(db);
    }
}

