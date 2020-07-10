package com.example.groupproject.soccer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "VideoDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "VIDEO_TABLE";
    public final static String COL_COUNTRY = "COUNTRY";
    public final static String COL_DATE = "DATE";
    public final static String COL_SIDE1 = "SIDE1";
    public final static String COL_SIDE2 = "SIDE2";
    public final static String COL_EMBED = "EMBED";
    public final static String COL_LIKE = "LIKE";
    public final static String COL_ID = "_id";

    public MyOpener(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "COUNTRY text, " +
                "DATE text, " +
                "SIDE1 text, " +
                "SIDE2 text, " +
                "EMBED, text" +
                "LIKE, int);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}
