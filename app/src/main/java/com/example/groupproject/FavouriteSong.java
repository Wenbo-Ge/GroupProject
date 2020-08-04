package com.example.groupproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FavouriteSong extends AppCompatActivity {
    private Intent goToIntent = new Intent();
    private SQLiteDatabase db;
    private List<ActivitySongDetails> songList = new ArrayList<ActivitySongDetails>();
    private MyListAdapter myListAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        loadDataFromDatabase();

        listView = findViewById(R.id.favouriteList);
        listView.setAdapter(myListAdapter = new MyListAdapter());

        listView.setOnItemClickListener((list, item, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setTitle("Do you want to remove this song?")
                    .setPositiveButton("Yes", (click, arg) -> {
                        deleteSong((ActivitySongDetails) listView.getItemAtPosition(position));
                        songList.remove(position);
                        myListAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg) -> {
                    })
                    .create().show();
        });

    }

    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return songList.size();
        }

        @Override
        public ActivitySongDetails getItem(int position) {
            return songList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return songList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ActivitySongDetails song = (ActivitySongDetails) getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.song_row, parent, false);

            TextView tittleText = newView.findViewById(R.id.songTitle);
            tittleText.setText(song.getTitle());


            TextView durationText = newView.findViewById(R.id.songDuration);
            durationText.setText(song.getDuration());

            TextView idText = newView.findViewById(R.id.songAlbumName);
            idText.setText(song.getAlbumName());

            TextView artistText = newView.findViewById(R.id.songTitle);
            artistText.setText(song.getArtist());
            return newView;
        }
    }//myListAdapter End

    protected void deleteSong(ActivitySongDetails activitySongDetails) {
        db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_ID + "= ?", new String[]{Long.toString(activitySongDetails.getId())});
    }

    private void loadDataFromDatabase() {
        //get a database connection:
        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer

//        protected final static String DATABASE_NAME = "SongsDB";
//        protected final static int VERSION_NUM = 1;
//        public final static String TABLE_NAME = "FAVOUR_SONG";
//        public final static String COL_TITLE = "TITLE";
//        public final static String COL_DURATION = "DURATION";
//        public final static String COL_ALBUMNAME = "ALBUMNAME";
//        public final static String COL_ALBUMCOVER = "ALBUMCOVER";
//        public final static String COL_ID = "_id";
//        public final static String COL_LINK = "LINK";
//        public final static String COL_FAVOURITE = "ISFAVOUTITE";
        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String[] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_TITLE, MyDatabaseOpenHelper.COL_DURATION, MyDatabaseOpenHelper.COL_ALBUMNAME, MyDatabaseOpenHelper.COL_ALBUMCOVER, MyDatabaseOpenHelper.COL_LINK, MyDatabaseOpenHelper.COL_FAVOURITE};
        //query all the results from the database:
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int tittleIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_TITLE);
        int durationIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_DURATION);
        int albumNameIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ALBUMNAME);
        int albumCoverIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ALBUMCOVER);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            String songTittle = results.getString(tittleIndex);
            String songDuration = results.getString(durationIndex);
            String songAlbumName = results.getString(albumNameIndex);
            String songAlbumCover = results.getString(albumCoverIndex);
            long id = results.getLong(idColIndex);

            songList.add(new ActivitySongDetails(id, songTittle, songDuration, songAlbumName, "", "", songAlbumCover,
                    false));

            Log.i("TITILE", songTittle);

        }

        //At this point, the contactsList array has loaded every row from the cursor.
    }
}
