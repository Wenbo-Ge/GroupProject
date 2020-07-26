package com.example.groupproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class SoccerSavedMatches extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SoccerMyOpener dbOpener;
    Cursor results;
    SQLiteDatabase db;
    ListView mySavedList;
    MyListAdapter mySavedAdapter;
    Toolbar tBar;

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
                        deleteVideo(mySavedAdapter.getItem(pos));
                        elements.remove(pos); //remove the match from list
                        mySavedAdapter.notifyDataSetChanged(); //there is one less item so update the list


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

        tBar = (Toolbar)findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);

        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.soccer_open, R.string.soccer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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

            Log.i("size", Integer.toString(elements.size()));
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the soccer_menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.soccer_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your soccer_menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the soccer_menu item is selected:
            case R.id.soccerMap:
                startActivity(new Intent(SoccerSavedMatches.this, ActivityLaunchGeo.class));
                break;
            case R.id.soccerSong:
                startActivity(new Intent(SoccerSavedMatches.this, ActivityLaunchSinger.class));
                break;
            case R.id.soccerLyrics:
                startActivity(new Intent(SoccerSavedMatches.this, ActivityLaunchLyrics.class));
                break;
            case R.id.soccerInfoField:
                message = "This is the Soccer activity, written by Wenbo Ge";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.soccerHeaderInstruction:
                AlertDialog.Builder builderInstruction = new AlertDialog.Builder(this);
                builderInstruction.setTitle("Instruction")
                        .setMessage("Please first login \n" +
                                "Select the match you are interested \n" +
                                "You can click 'Watch your match' to checked saved matches") //add the 3 edit texts showing the contact information
                        .setNegativeButton("dismiss", (click, b) -> { })
                        .create().show();
                break;
            case R.id.soccerHeaderAPI:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.scorebat.com/video-api/v1/")));
                break;
            case R.id.soccerHeaderDonate:
                AlertDialog.Builder builderDonate = new AlertDialog.Builder(this);
                builderDonate.setView(getLayoutInflater().inflate(R.layout.activity_soccer_donate, null))
                        .setNegativeButton("Cancel", (click, b) -> { })
                        .setPositiveButton("Thank you", (click, b) -> { })
                        .create().show();
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}