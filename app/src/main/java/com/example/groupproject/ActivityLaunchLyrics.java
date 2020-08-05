package com.example.groupproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ActivityLaunchLyrics extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    public static final String ARTIST = "ARTIST";
    public static final String TITLE = "TITLE";
    public static final String LYRICS = "LYRICS";
    //public static final String FAVORITE = "FAVORITE";
    public static final String ID = "ID";
    static final int REQUEST_FAVORITE_STATUS = 1;
    private EditText artistInput;
    private EditText titleInput;
    private Button search;
    private CheckBox loadLastSearch;
    private ProgressBar pb;
    private ArrayList<LyricsResult> favoriteList = new ArrayList<>();
    private LyricsResult current;
    private FavoriteListAdapter adapter;
    private SQLiteDatabase db;
    private ListView favoriteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_lyrics);
        loadDataFromDatabase();
        search = findViewById(R.id.buttonLSSearch);
        search.setOnClickListener(v -> executeSearch());
        artistInput = findViewById(R.id.editTextLSArtist);
        titleInput = findViewById(R.id.editTextLSTitle);
        pb = findViewById(R.id.progressBarLSSearch);
        favoriteView = findViewById(R.id.listViewLSFavorites);
        favoriteView.setAdapter(adapter = new FavoriteListAdapter());
        favoriteView.setOnItemLongClickListener((p, b, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getString(R.string.alertTitleLSFavList))
                    .setMessage(getString(R.string.textViewLSArtist) + ": " + adapter.getItem(pos).getArtist() + "\n" +
                            getString(R.string.textViewLSTitle) + ": " + adapter.getItem(pos).getTitle())
                    .setPositiveButton(getString(R.string.buttonLSAlertYes), (click, arg) -> {
                        favoriteList.remove(pos);
                        db.delete(LyricsFavoriteOpener.TABLE_NAME, LyricsFavoriteOpener.COL_ID + "= ?", new String[] {Long.toString(id)});
                        adapter.notifyDataSetChanged();
                    })
                    //What the No button does:
                    .setNegativeButton(getString(R.string.buttonLSAlertNo), (click, arg) -> { })
                    //Show the dialog
                    .create().show();
            return true;
        });

        favoriteView.setOnItemClickListener((p, b, pos, id) -> {
            openResult(favoriteList.get(pos));
        });

        loadLastSearch = findViewById(R.id.checkboxLSLastSearch);
        loadLastSearch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SharedPreferences prefs = getSharedPreferences("lyrics_search", Context.MODE_PRIVATE);
                artistInput.setText(prefs.getString(ARTIST, ""));
                artistInput.setEnabled(false);
                titleInput.setText(prefs.getString(TITLE, ""));
                titleInput.setEnabled(false);
            } else {
                artistInput.setText("");
                artistInput.setEnabled(true);
                titleInput.setText("");
                titleInput.setEnabled(true);
            }
        });

        Toolbar tBar = (Toolbar)findViewById(R.id.toolbarLSLaunch);
        setSupportActionBar(tBar);

        DrawerLayout drawer = findViewById(R.id.LSdrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.LSnavdrawerOpen, R.string.LSnavdrawerClose);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navViewLS);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lyrics_search_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent goTo = null;
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.menuLSabout:
                Toast.makeText(this, R.string.LSabout, Toast.LENGTH_LONG).show();
                break;
            case R.id.menuLSdeezer:
                goTo = new Intent(ActivityLaunchLyrics.this, ActivityLaunchSinger.class);
                break;
            case R.id.menuLSgeo:
                goTo = new Intent(ActivityLaunchLyrics.this, ActivityLaunchGeo.class);
                break;
            case R.id.menuLSsoccer:
                goTo = new Intent(ActivityLaunchLyrics.this, ActivityLaunchSoccer.class);
                break;
        }
        if (goTo != null) startActivity(goTo);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        AlertDialog.Builder alertDialogBuilder;
        switch(item.getItemId()) {
            case R.id.menuLSInstruction:
                alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getString(R.string.alertHelpTitleLS))
                        .setMessage(getString(R.string.alertHelpTextLS))
                        .setPositiveButton(getString(R.string.buttonLSAlertOk), (click, arg) -> {})
                        .create().show();
                break;
            case R.id.menuLSAboutAPI:
                Uri uri = Uri.parse("https://lyricsovh.docs.apiary.io/#");

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(this.getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.menuLSDonate:
                alertDialogBuilder = new AlertDialog.Builder(this);
                final View customLayout = getLayoutInflater().inflate(R.layout.lyrics_search_donate_dialog, null);
                alertDialogBuilder.setView(customLayout);
                alertDialogBuilder.setTitle(getString(R.string.alertDonateTitleLS))
                        .setMessage(getString(R.string.alertDonateMessageLS))
                        .setPositiveButton(getString(R.string.buttonLSAlertThankYou), (click, arg) -> {})
                        .setNegativeButton(getString(R.string.buttonLSAlertCancel), (click, arg) -> { })
                        .create().show();
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.LSdrawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        //Toast.makeText(this, "NavigationDrawer: " + message, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FAVORITE_STATUS && data != null) {
            long id = data.getLongExtra(ID, 0);

            if (current.getId() != 0 && current.getId() != id) {
                // entry was removed from DB, remove from list
                for(LyricsResult r : favoriteList) {
                    if (r.getId() == current.getId()) {
                        favoriteList.remove(r);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            if (id > 0) {
                favoriteList.add(new LyricsResult(current.getArtist(),
                        current.getTitle(), current.getLyrics(), id));
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pb.setVisibility(View.INVISIBLE);
        search.setEnabled(true);
    }

    // transit to the next activity with lyrics result loaded from favourite list or async task
    private void openResult(LyricsResult lr) {
        Bundle dataToPass = new Bundle();
        dataToPass.putString(ARTIST, lr.getArtist());
        dataToPass.putString(TITLE, lr.getTitle());
        dataToPass.putString(LYRICS, lr.getLyrics());
        dataToPass.putLong(ID, lr.getId());
        current = lr;
        Intent goToResult = new Intent(ActivityLaunchLyrics.this, LyricsResultActivity.class);
        goToResult.putExtras(dataToPass);
        startActivityForResult(goToResult, REQUEST_FAVORITE_STATUS);
    }

    // event handler for the search button: search lyrics locally first. if not found execute the async task
    private void executeSearch() {
        String artist = artistInput.getText().toString().trim();
        String title = titleInput.getText().toString().trim();

        SharedPreferences prefs = getSharedPreferences("lyrics_search", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ARTIST, artist);
        editor.putString(TITLE, title);
        editor.commit();

        if (artist.length() == 0 || title.length() == 0) {
            Toast.makeText(ActivityLaunchLyrics.this, getResources().getString(R.string.toastLSInvalidInput), Toast.LENGTH_LONG ).show();
        } else {
            search.setEnabled(false);
            LyricsResult lr = localSearch(artist, title);
            if (lr == null) {
                LyricsSearchRequest req = new LyricsSearchRequest();
                req.execute(new String[]{artist, title});
                pb.setVisibility(View.VISIBLE);
            } else {
                openResult(lr);
            }
        }
    }

    // local search, search lyrics in favourite list
    private LyricsResult localSearch(String artist, String title) {
        for (LyricsResult lr : favoriteList) {
            if (lr.getArtist().equalsIgnoreCase(artist) && lr.getTitle().equalsIgnoreCase(title)) return lr;
        }
        return null;
    }

    //Capitalize the first letter of every word in song name
    private static String capitalize(String s) {
        String words[]=s.split("\\s");
        StringBuilder sb = new StringBuilder();
        for(String w:words){
            String first=w.substring(0,1);
            String rest=w.substring(1);
            sb.append(first.toUpperCase()).append(rest).append(" ");
        }
        return sb.toString().trim();
    }

    // async task to download/ search lyrics online
    private class LyricsSearchRequest extends AsyncTask<String, Integer, String> {

        private boolean success = false;
        private String artist;
        private String title;

        public String doInBackground(String ... args) {
            HttpURLConnection urlConnection = null;
            try {
                artist = capitalize(args[0]);
                title = capitalize(args[1]);
                //create a URL object of what server to contact:
                URL url = new URL("https://api.lyrics.ovh/v1/" + android.net.Uri.encode(artist, "utf-8" )  + "/" + android.net.Uri.encode(title, "utf-8"));
                //open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string

                JSONObject resultObject = new JSONObject(result);
                String lyrics = resultObject.getString("lyrics");
                if (lyrics.length() > 0) {
                    success = true;
                }
                return lyrics;
            } catch (Exception e) {
                String m = "Failed to search lyrics due to error: " + e.getMessage();
                Log.e("HTTP", m);
                return m;
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
        }


        public void onProgressUpdate(Integer ... args) {}

        public void onPostExecute(String fromDoInBackground) {
            Log.i("HTTP", fromDoInBackground);
            if (success) {
                LyricsResult lr = new LyricsResult(artist, title, fromDoInBackground, 0);
                openResult(lr);
            } else {
                pb.setVisibility(View.INVISIBLE);
                search.setEnabled(true);
                Toast.makeText(ActivityLaunchLyrics.this, getResources().getString(R.string.toastLSNotFound), Toast.LENGTH_LONG ).show();
            }
        }
    }

    // load favourite list from database on startup
    private void loadDataFromDatabase() {
        //get a database connection:
        LyricsFavoriteOpener dbOpener = new LyricsFavoriteOpener(this);
        db = dbOpener.getWritableDatabase();


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {LyricsFavoriteOpener.COL_ID, LyricsFavoriteOpener.COL_ARTIST, LyricsFavoriteOpener.COL_TITLE, LyricsFavoriteOpener.COL_LYRICS};
        //query all the results from the database:
        Cursor results = db.query(false, LyricsFavoriteOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int artistColumnIndex = results.getColumnIndex(LyricsFavoriteOpener.COL_ARTIST);
        int titleColIndex = results.getColumnIndex(LyricsFavoriteOpener.COL_TITLE);
        int lyricsColIndex = results.getColumnIndex(LyricsFavoriteOpener.COL_LYRICS);
        int idColIndex = results.getColumnIndex(LyricsFavoriteOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            favoriteList.add(new LyricsResult(
                    results.getString(artistColumnIndex),
                    results.getString(titleColIndex),
                    results.getString(lyricsColIndex),
                    results.getLong(idColIndex)
            ));
        }
    }


    // list adaptor for the favorite list view
    private class FavoriteListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return favoriteList.size();
        }

        @Override
        public LyricsResult getItem(int position) {
            return favoriteList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();

            LyricsResult lf = getItem(position);
            //make a new row:
            View newView = inflater.inflate(R.layout.lyrics_favorite_row_layout, parent, false);
            //set what the text should be for this row:
            TextView tView = newView.findViewById(R.id.textViewLSFavoriteListArtist);
            tView.setText("   " + lf.getArtist());
            tView = newView.findViewById(R.id.textViewLSFavoriteListTitle);
            tView.setText("   " + lf.getTitle());
            //return it to be put in the table
            return newView;
        }
    }
}