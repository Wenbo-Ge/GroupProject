package com.example.groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ActivityLaunchLyrics extends AppCompatActivity {

    public static final String ARTIST = "ARTIST";
    public static final String TITLE = "TITLE";
    public static final String LYRICS = "LYRICS";
    public static final String FAVORITE = "FAVORITE";
    private EditText artistInput;
    private EditText titleInput;
    private Button search;
    private ProgressBar pb;
    private ArrayList<LyricsFavorite> favoriteList = new ArrayList<>();
    private FavoriteListAdapter adapter;
    private SQLiteDatabase db;
    private ListView favoriteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_lyrics);
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
                        //db.delete(MessageOpener.TABLE_NAME, MessageOpener.COL_ID + "= ?", new String[] {Long.toString(id)});
                        adapter.notifyDataSetChanged();
                    })
                    //What the No button does:
                    .setNegativeButton(getString(R.string.buttonLSAlertNo), (click, arg) -> { })
                    //Show the dialog
                    .create().show();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pb.setVisibility(View.INVISIBLE);
        search.setEnabled(true);
    }

    private void executeSearch() {
        String artist = artistInput.getText().toString().trim();
        String title = titleInput.getText().toString().trim();
        if (artist.length() == 0 || title.length() == 0) {
            Toast.makeText(ActivityLaunchLyrics.this, getResources().getString(R.string.toastLSInvalidInput), Toast.LENGTH_LONG ).show();
        } else {
            search.setEnabled(false);
            LyricsSearchRequest req = new LyricsSearchRequest();
            req.execute(new String[]{artist, title});
            pb.setVisibility(View.VISIBLE);
        }



    }

    private class LyricsFavorite {

        private final String artist;
        private final String title;
        private final String lyrics;
        private final long id;

        public LyricsFavorite(String artist, String title, String lyrics, long id) {
            this.artist = artist;
            this.title = title;
            this.lyrics = lyrics;
            this.id = id;
        }

        public String getLyrics() {
            return lyrics;
        }

        public String getArtist() {
            return artist;
        }

        public String getTitle() {
            return title;
        }

        public long getId() {
            return id;
        }
    }

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
                URL url = new URL("https://api.lyrics.ovh/v1/" + artist.replaceAll("\\s+", "%20") + "/" + title.replaceAll("\\s+", "%20"));

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
                success = true;
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
                Bundle dataToPass = new Bundle();
                dataToPass.putString(ARTIST, artist);
                dataToPass.putString(TITLE, title);
                dataToPass.putString(LYRICS, fromDoInBackground);
                dataToPass.putBoolean(FAVORITE, false);
                Intent goToResult = new Intent(ActivityLaunchLyrics.this, LyricsResultActivity.class);
                goToResult.putExtras(dataToPass);
                startActivity(goToResult);
            }
        }
    }
    //startActivity(new Intent(ActivityLaunchLyrics.this, LyricsResultActivity.class));


    private class FavoriteListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return favoriteList.size();
        }

        @Override
        public LyricsFavorite getItem(int position) {
            return favoriteList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();

            LyricsFavorite lf = getItem(position);
            //make a new row:
            View newView = inflater.inflate(R.layout.lyrics_favorite_row_layout, parent, false);
            //set what the text should be for this row:
            TextView tView = newView.findViewById(R.id.textViewLSFavoriteListArtist);
            tView.setText(lf.getArtist());
            tView = newView.findViewById(R.id.textViewLSFavoriteListTitle);
            tView.setText(lf.getTitle());
            //return it to be put in the table
            return newView;
        }
    }
}