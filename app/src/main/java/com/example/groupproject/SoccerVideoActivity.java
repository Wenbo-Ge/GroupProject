package com.example.groupproject;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class SoccerVideoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    MyListAdapter myAdapter;
    SoccerMyOpener dbOpener;
    SQLiteDatabase db;
    ListView myList;
    Toolbar tBar;

    Button yourMatchButton;

    private ArrayList<SoccerVideo> elements = new ArrayList<>( Arrays.asList());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_match_highlights);

        matchQuery req = new matchQuery();
        req.execute();

        dbOpener = new SoccerMyOpener(this);
        db = dbOpener.getWritableDatabase();

        myList =  findViewById(R.id.theListView);

        yourMatchButton = findViewById(R.id.matches);


        yourMatchButton.setOnClickListener(bt -> {
            startActivity(new Intent(SoccerVideoActivity.this, SoccerSavedMatches.class));
        });


        myList.setOnItemClickListener( (parent, view, pos, id) -> {
            showDetail( pos );
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

    protected void showDetail(int pos)
    {
        SoccerVideo selectedMatch = elements.get(pos);

        View detail_view = getLayoutInflater().inflate(R.layout.activity_soccer_detail, null);

        /**
         * get the TextViews
         */


        TextView countryView = detail_view.findViewById(R.id.country);
        TextView dateView = detail_view.findViewById(R.id.date);
        TextView side1View = detail_view.findViewById(R.id.textViewA);
        TextView side2View = detail_view.findViewById(R.id.textViewB);

        /**
         * set the fields for the alert dialog
         */

        countryView.setText(selectedMatch.getCountry());
        dateView.setText(selectedMatch.getDate());
        side1View.setText(selectedMatch.getSide1());
        side2View.setText(selectedMatch.getSide2());



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Match Details")
                .setView(detail_view)
                .setNegativeButton("dismiss", (click, b) -> { })
                .setPositiveButton("save", (click, b) -> {
                    ContentValues newRowValues = new ContentValues();

                    /**
                     * add to database
                     */
                    newRowValues.put(SoccerMyOpener.COL_COUNTRY, selectedMatch.getCountry());
                    newRowValues.put(SoccerMyOpener.COL_TITLE, selectedMatch.getTitle());
                    newRowValues.put(SoccerMyOpener.COL_DATE, selectedMatch.getDate());
                    newRowValues.put(SoccerMyOpener.COL_SIDE1, selectedMatch.getSide1());
                    newRowValues.put(SoccerMyOpener.COL_SIDE2, selectedMatch.getSide2());
                    newRowValues.put(SoccerMyOpener.COL_EMBED, selectedMatch.getEmbed());

                   db.insert(SoccerMyOpener.TABLE_NAME, null, newRowValues);

                })
                .setNeutralButton("watch highlight", (click, b) -> {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(selectedMatch.getEmbed())));
                })
//                .setNeutralButton("dismiss", (click, b) -> { })
                .create().show();
    }

    private class matchQuery extends AsyncTask< String, Integer, String>
    {

        String title, side1, side2, country, matchDate, videoUrl;

        //Type3                Type1
        protected String doInBackground(String ... args)
        {
            try {

                /**
                 * create a URL object of what server to contact:
                 */


                URL matchUrl= new URL("https://www.scorebat.com/video-api/v1/");


                /**
                 * open the connection, wait for data:
                 */

                HttpURLConnection urlMatchConnection = (HttpURLConnection) matchUrl.openConnection();


                InputStream responseMatch = urlMatchConnection.getInputStream();



                /**
                 * JSON reading:  Build the entire string response:
                 */

                BufferedReader reader = new BufferedReader(new InputStreamReader(responseMatch, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;

                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");

                }
                String result = sb.toString(); //result is the whole string
                Log.i("result", result);




                /**
                 * convert string to JSON
                 */
                try {
                    JSONArray matchArray = new JSONArray(result);
                    for (int i = 0; i < matchArray.length(); i ++) {
                        JSONObject matchJson = matchArray.getJSONObject(i);

                    JSONObject side1Json = matchJson.getJSONObject("side1");
                    JSONObject side2Json = matchJson.getJSONObject("side1");
                    JSONObject competitionJson = matchJson.getJSONObject("competition");


                    title = matchJson.getString("title");
                    side1 = side1Json.getString("name");
                    side2 = side2Json.getString("name");
                    country = competitionJson.getString("name");
                    matchDate = matchJson.getString("date");
                    videoUrl = matchJson.getString("url");

                    SoccerVideo soccerVideo = new SoccerVideo(country, title, matchDate, side1, side2, videoUrl, i);

                    elements.add(soccerVideo);
                    }
                    myAdapter.notifyDataSetChanged();
                } catch (Throwable t){
                    Log.e("JSON Error", "error");
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return "Done";
        }

        //Type 2


        public void onProgressUpdate(Integer ... value)
        {

        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            myList.setAdapter( myAdapter = new MyListAdapter() );

        }
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();
        startActivity(new Intent(SoccerVideoActivity.this, ActivityLaunchSoccer.class));
        finish();
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

        switch(item.getItemId())
        {

            case R.id.soccerMap:
                startActivity(new Intent(SoccerVideoActivity.this, ActivityLaunchGeo.class));
                break;
            case R.id.soccerSong:
                startActivity(new Intent(SoccerVideoActivity.this, ActivityLaunchSinger.class));
                break;
            case R.id.soccerLyrics:
                startActivity(new Intent(SoccerVideoActivity.this, ActivityLaunchLyrics.class));
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
                                "You can click 'Watch your match' to checked saved matches")
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




//    protected void printCursor (Cursor c, int version) {
//        int messageColumnIndex = c.getColumnIndex(MyOpener.COL_MESSAGE);
//        int idColIndex = c.getColumnIndex(MyOpener.COL_ID);
//        int sentColIndex = c.getColumnIndex(MyOpener.COL_SENT);
//
//        String message = c.getString(messageColumnIndex);
//        long id = c.getLong(idColIndex);
//        boolean sent = c.getInt(sentColIndex) == 1;
//        Log.i("Version Number is: ", Integer.toString(version));
//        Log.i("Number of columns: ", Integer.toString(c.getColumnCount()));
//        Log.i("Name of columns: ", Arrays.toString(c.getColumnNames()));
//        Log.i("Number of rows: ", Integer.toString(c.getCount()));
//        Log.i("Results of row: ", MyOpener.COL_ID + " " +id + " " + MyOpener.COL_MESSAGE +" " + message
//                + " " + MyOpener.COL_SENT + " " +sent);
//
//    }
}
