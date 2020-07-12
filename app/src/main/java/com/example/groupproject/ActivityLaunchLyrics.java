package com.example.groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityLaunchLyrics extends AppCompatActivity {

    private EditText artistInput;
    private EditText titleInput;
    private ProgressBar pb;
    private ArrayList<LyricsFavorite> favoriteList = new ArrayList<>();
    private FavoriteListAdapter adapter;
    private SQLiteDatabase db;
    private ListView favoriteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_lyrics);
        Button search = findViewById(R.id.buttonLSSearch);
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

    private void executeSearch() {
        String artist = artistInput.getText().toString();
        String title = titleInput.getText().toString();
        if (artist.length() == 0 || title.length() == 0) {
            Toast.makeText(ActivityLaunchLyrics.this, getResources().getString(R.string.toastLSInvalidInput), Toast.LENGTH_LONG ).show();
        } else {
            startActivity(new Intent(ActivityLaunchLyrics.this, LyricsResultActivity.class));
        }

    }

    private class LyricsFavorite {

        private String artist;
        private String title;

        public LyricsFavorite(String artist, String title) {
            this.artist = artist;
            this.title = title;
        }

        public String getArtist() {
            return artist;
        }

        public String getTitle() {
            return title;
        }
    }

    private class FavoriteListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public LyricsFavorite getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}