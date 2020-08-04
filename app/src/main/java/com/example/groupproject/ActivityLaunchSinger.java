package com.example.groupproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class ActivityLaunchSinger extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private List<ActivitySongDetails> songList = new ArrayList<ActivitySongDetails>();
    private MyListAdapter myListAdapter;
    private ListView listView;
    private String urlString;
    private Bitmap image;
    private String tittleContent;
    private String durationCol;
    private String albumNameCol;
    private String albumCoverCol;
    MyDatabaseOpenHelper dbOpener;
    Cursor results;
    SQLiteDatabase db;
    ProgressBar progressBar;
    private Intent ActivityLaunchSinger;
    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_singer);


        boolean isTablet = findViewById(R.id.fragmentLayout) != null;
        listView = findViewById(R.id.songListView);
        listView.setAdapter(myListAdapter = new MyListAdapter());

        SharedPreferences prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = prefs.getString("ReserveName", "Beatles");

        EditText editText = findViewById(R.id.searchSingerHintMessage);
        editText.setText(savedString);
        editText.setText(editText.getText().toString());

        Toolbar toolbar=findViewById(R.id.toolbarLaunch);
         setSupportActionBar(toolbar);

         DrawerLayout drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.navdrawerOpen, R.string.navdrawerClose);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);


        Button searchButton;
        EditText searchField= null;




        Button searchButton = findViewById(R.id.searchSingerButtonText);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        searchButton.setOnClickListener(v -> {

            progressBar.setVisibility(View.VISIBLE);
            String message1 = "Please enter a name!!!";
            String message2 = "Incorrect format!!!";
            if (editText.getText().toString().matches("")) {
                Toast.makeText(ActivityLaunchSinger.this, message1, Toast.LENGTH_LONG).show();
            } else if (editText.getText().toString().matches("^[a-zA-Z]+$")) {
                songList.clear();
                String artist = editText.getText().toString();
                try {
                    urlString = "https://api.deezer.com/search/artist/?q=" + URLEncoder.encode(artist, "UTF-8") + "&output=xml";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                DeezerSongQuery SongQuery = new DeezerSongQuery();
                SongQuery.execute(urlString);
            } else {
                Snackbar.make(searchButton, message2, Snackbar.LENGTH_LONG).show();
            }

        });
        Button favourButton = findViewById(R.id.FavoritesButtonText);
        favourButton.setOnClickListener(v -> {
            Intent goToFavour=new Intent(ActivityLaunchSinger.this, FavouriteSong.class);
            startActivity(goToFavour);
                });

        listView.setOnItemClickListener((list, item, position, id) -> {
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString(MyDatabaseOpenHelper.COL_TITLE, songList.get(position).getTitle());
            dataToPass.putString(MyDatabaseOpenHelper.COL_ALBUMCOVER, songList.get(position).getAlbumCover());
            dataToPass.putString(MyDatabaseOpenHelper.COL_DURATION, songList.get(position).getDuration());
            dataToPass.putString(MyDatabaseOpenHelper.COL_ALBUMNAME, songList.get(position).getAlbumName());
            if (isTablet) {
                FragmentSongDetails dFragment = new FragmentSongDetails();
                dFragment.setArguments(dataToPass);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLayout, dFragment)
                        .addToBackStack("Any")
                        .commit();
            } else {
                Intent toPhone = new Intent(this, EmptyActivity.class);
                toPhone.putExtras(dataToPass);
                startActivity(toPhone);
            }


        });


    }// onCreate end


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu,menu);


//        MenuItem searchItem = menu.findItem(R.id.search);
//        SearchView sView = (SearchView)searchItem.getActionView();
//        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//              return true;
//
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;	}  });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        String message = null;
        switch(item.getItemId())
        {
//            case R.id.menuGeo:
//                startActivity(new Intent(ActivityLaunchSinger.this, ActivityLaunchGeo.class));
//                break;
//            case R.id.menusoccer:
//                startActivity(new Intent(ActivityLaunchSinger.this, ActivityLaunchSoccer.class));
//                break;
//            case R.id.menuLyrics:
//                startActivity(new Intent(ActivityLaunchSinger.this, ActivityLaunchLyrics.class));
//                break;
            case R.id.menuAbout:
                message = "This is the Singer activity, written by Lifeng Yao";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder alertDialogBuilder;
        switch(item.getItemId()) {
            case R.id.menuInstruction:
                alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getString(R.string.alertHelp))
                        .setMessage(getString(R.string.alertHelpText))
                        .setPositiveButton(getString(R.string.buttonAlertOk), (click, arg) -> {})
                        .create().show();
                break;
            case R.id.menuAbout:
                Uri uri = Uri.parse("https://www.deezer.com/search/artist");

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(this.getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.menuDonate:
                AlertDialog.Builder builderDonate = new AlertDialog.Builder(this);
                builderDonate.setView(getLayoutInflater().inflate(R.layout.activity_singer_donate, null))
                        .setNegativeButton("Next Time", (click, b) -> { })
                        .setPositiveButton("Of Course", (click, b) -> { })
                        .create().show();
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.closeDrawer(GravityCompat.START);

        //Toast.makeText(this, "NavigationDrawer: " + message, Toast.LENGTH_LONG).show();
        return false;

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
        db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_ID + "= ?", new String[] {Long.toString(activitySongDetails.getId())});
    }
    protected class DeezerSongQuery extends AsyncTask<String, Integer, String> {
        private List<ActivitySongDetails> songResults = new ArrayList<>();
        String trackURL;
        List<String> trackList = new ArrayList<>();
        String ret;

        @Override
        protected String doInBackground(String... args) {
            try {

                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream response = urlConnection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {

                        String xmlTagName = xpp.getName();
                        if (xmlTagName.equals("tracklist")) {

                            trackURL = xpp.nextText();
                            trackList.add(trackURL);

                        }
                    }
                    eventType = xpp.next();
                }

                URL url2 = new URL(trackList.get(0));

                HttpURLConnection url2Connection = (HttpURLConnection) url2.openConnection();

                InputStream responseSong = url2Connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(responseSong, "UTF-8"), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                String result = stringBuilder.toString();

                JSONObject songJSONObject = new JSONObject(result);
                JSONArray songJSONArray = songJSONObject.getJSONArray("data");

                for (int i = 0; i < songJSONArray.length(); i++) {
                    JSONObject thisSong = songJSONArray.getJSONObject(i);
                    String title = thisSong.getString("title_short");
                    String duration = thisSong.getString("duration");
                    String artist = thisSong.getString("artist");

                    JSONObject thisArtist = new JSONObject(artist);
                    String artistName = thisArtist.getString("name");

                    String album = thisSong.getString("album");
                    JSONObject thisAlbum = new JSONObject(album);
                    String albumTitle = thisAlbum.getString("title");

//                    if (fileExistence(title)) {
//                        FileInputStream inputStream = null;
//                        try {
//                            inputStream = new FileInputStream(getBaseContext().getFileStreamPath(title));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        image = BitmapFactory.decodeStream(inputStream);
//                    } else {
//                        URL iconUrl = new URL(thisAlbum.getString("cover"));
//                        image = getImage(iconUrl);
//                        FileOutputStream outputStream = openFileOutput(title + ".png", Context.MODE_PRIVATE);
//                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
//                        outputStream.flush();
//                        outputStream.close();
//                    }

                    ActivitySongDetails song = new ActivitySongDetails();
                    song.setTitle(title);
                    song.setDuration(duration);
                    song.setArtist(artistName);
                    song.setAlbumName(albumTitle);
                    song.setAlbumCover(thisAlbum.getString("cover"));
                    songResults.add(song);
                }

                publishProgress(100);
                progressBar.setProgress(100);

            } catch (MalformedURLException mfe) {
                ret = "Malformed URL exception";
            } catch (IOException ioe) {
                ret = "IO Exception. Is the Wifi connected?";
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return "Done";
        }

//        public boolean fileExistence(String fname) {
//            File file = getBaseContext().getFileStreamPath(fname);
//            return file.exists();
//        }

//        protected Bitmap getImage(URL url) {
//            Log.i("WeatherForecast", "getImage");
//            HttpURLConnection iconConn = null;
//            try {
//                iconConn = (HttpURLConnection) url.openConnection();
//                iconConn.connect();
//                int response = iconConn.getResponseCode();
//                if (response == 200) {
//                    return BitmapFactory.decodeStream(iconConn.getInputStream());
//                } else {
//                    return null;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            } finally {
//                if (iconConn != null) {
//                    iconConn.disconnect();
//                }
//            }
//        }

        @Override
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            songList.addAll(this.songResults);
            myListAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
        }


    }


}


