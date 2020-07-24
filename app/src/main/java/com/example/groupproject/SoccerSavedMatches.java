package com.example.groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class SoccerSavedMatches extends AppCompatActivity {

    SoccerMyOpener dbOpener;
    Cursor results;
    SQLiteDatabase db;
    ListView mySavedList;
    MyListAdapter mySavedAdapter;
    public static final String ITEM_SELECTED_HTML = "videoHTML";

    private ArrayList<SoccerVideo> elements = new ArrayList<>( Arrays.asList());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_saved_matches);

        mySavedList =  findViewById(R.id.theListViewSaved);

        loadDataFromDatabase();

        mySavedList.setAdapter( mySavedAdapter = new MyListAdapter());

        mySavedList.setOnItemLongClickListener( (parent, view, pos, id) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Unsave this match?")
                    .setNegativeButton("Delete", (click, b) -> {

                        elements.remove(pos); //remove the contact from contact list
                        mySavedAdapter.notifyDataSetChanged(); //there is one less item so update the list
                        deleteVideo(mySavedAdapter.getItem(pos));

                    })
                    .setNeutralButton("dismiss", (click, b) -> { })
                    .create().show();
                    return true;
                });

        mySavedList.setOnItemClickListener((list, item, position, id) -> {
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED_HTML, elements.get(position).getEmbed() );


            SoccerDetailsFragment dFragment = new SoccerDetailsFragment(); //add a DetailFragment
            dFragment.setArguments( dataToPass ); //pass it a bundle for information
            getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment

        });


    }

    private void loadDataFromDatabase()
    {

        dbOpener = new SoccerMyOpener(this);
        db = dbOpener.getWritableDatabase();


        String [] columns = {SoccerMyOpener.COL_ID, SoccerMyOpener.COL_COUNTRY, SoccerMyOpener.COL_TITLE, SoccerMyOpener.COL_DATE, SoccerMyOpener.COL_SIDE1, SoccerMyOpener.COL_SIDE2, SoccerMyOpener.COL_EMBED};

        results = db.query(false, SoccerMyOpener.TABLE_NAME, columns, null, null, null, null, null, null);


        int countryColumnIndex = results.getColumnIndex(SoccerMyOpener.COL_COUNTRY);
        int titleColumnIndex = results.getColumnIndex(SoccerMyOpener.COL_TITLE);
        int dateColumnIndex = results.getColumnIndex(SoccerMyOpener.COL_DATE);
        int side1ColumnIndex = results.getColumnIndex(SoccerMyOpener.COL_SIDE1);
        int side2ColumnIndex = results.getColumnIndex(SoccerMyOpener.COL_SIDE2);
        int embedColumnIndex = results.getColumnIndex(SoccerMyOpener.COL_EMBED);
        int idColIndex = results.getColumnIndex(SoccerMyOpener.COL_ID);


        while(results.moveToNext())
        {
            String country = results.getString(countryColumnIndex);
            String title = results.getString(titleColumnIndex);
            String date = results.getString(dateColumnIndex);
            String side1 = results.getString(side1ColumnIndex);
            String side2 = results.getString(side2ColumnIndex);
            String embed = results.getString(embedColumnIndex);
            long id = results.getLong(idColIndex);


            elements.add(new SoccerVideo(country, title, date, side1, side2, embed, id));
//            printCursor(results,db.getVersion());

        }

    }

    private class MyListAdapter extends BaseAdapter {

        public int getCount() { return elements.size();}

        public SoccerVideo getItem(int position) { return elements.get(position); }

        public long getItemId(int position) { return getItem(position).getId(); }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            SoccerVideo soccerVideo = getItem(position);

            TextView matchTitle;


            convertView = inflater.inflate(R.layout.activity_soccer_row_layout, parent, false);


            matchTitle = convertView.findViewById(R.id.matchTitle);


            matchTitle.setText(soccerVideo.getTitle());
            return convertView;
        }
    }

    protected void deleteVideo(SoccerVideo m)
    {
        db.delete(SoccerMyOpener.TABLE_NAME, SoccerMyOpener.COL_ID + "= ?", new String[] {Long.toString(m.getId())});
    }
}