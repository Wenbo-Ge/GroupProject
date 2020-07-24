package com.example.groupproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LyricsResultFragment extends Fragment {

    private AppCompatActivity parentActivity;
    private LyricsResult lr;
    private SQLiteDatabase db;
    Intent returnIntent = new Intent();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_lyrics_result, container, false);

        Bundle dataFromActivity = getArguments();
        String artist = dataFromActivity.getString(ActivityLaunchLyrics.ARTIST);
        String title = dataFromActivity.getString(ActivityLaunchLyrics.TITLE);
        String lyrics = dataFromActivity.getString(ActivityLaunchLyrics.LYRICS);
        long id = dataFromActivity.getLong(ActivityLaunchLyrics.ID, 0);
        lr = new LyricsResult(artist, title, lyrics, id);

        TextView tv = result.findViewById(R.id.textViewLSTitle);
        tv.setText(title);
        tv = result.findViewById(R.id.textViewLSArtist);
        tv.setText(artist);
        tv = result.findViewById(R.id.textViewLSLyrics);
        tv.setText(lyrics);


        ToggleButton favorite = result.findViewById(R.id.toggleButtonLSFavorite);
        boolean isFavorite = id > 0;
        favorite.setChecked(isFavorite);
        favorite.setBackgroundDrawable(ContextCompat.getDrawable(parentActivity.getApplicationContext(), isFavorite ? R.drawable.heart_filled : R.drawable.heart_empty));
        // click event handler for favorite toggle button
        favorite.setOnCheckedChangeListener((v, isChecked) -> {
            if (db == null) {
                LyricsFavoriteOpener dbOpener = new LyricsFavoriteOpener(parentActivity);
                db = dbOpener.getWritableDatabase();
            }
            if (isChecked) {
                ContentValues newRowValues = new ContentValues();

                newRowValues.put(LyricsFavoriteOpener.COL_ARTIST, lr.getArtist());
                newRowValues.put(LyricsFavoriteOpener.COL_TITLE, lr.getTitle());
                newRowValues.put(LyricsFavoriteOpener.COL_LYRICS, lr.getLyrics());

                long newId = db.insert(LyricsFavoriteOpener.TABLE_NAME, null, newRowValues);
                lr = new LyricsResult(lr.getArtist(), lr.getTitle(), lr.getLyrics(), newId);
            } else {
                db.delete(LyricsFavoriteOpener.TABLE_NAME, LyricsFavoriteOpener.COL_ID + "= ?", new String[] {Long.toString(lr.getId())});
                lr = new LyricsResult(lr.getArtist(), lr.getTitle(), lr.getLyrics(), 0);
            }
            favorite.setBackgroundDrawable(ContextCompat.getDrawable(parentActivity.getApplicationContext(), isChecked ? R.drawable.heart_filled : R.drawable.heart_empty));
            Snackbar.make(favorite, getResources().getString(isChecked ? R.string.snackbarLSFavoriteOn : R.string.snackbarLSFavoriteOff), Snackbar.LENGTH_SHORT)
                    .setAction(getResources().getString(R.string.snackbarLSFavoriteUndo),click -> favorite.setChecked(!isChecked)).show();
            // Pass favoriate/db status back to previous activity
            returnIntent.putExtra(ActivityLaunchLyrics.ID, lr.getId());
            parentActivity.setResult(Activity.RESULT_OK,returnIntent);
        });
        Button b = result.findViewById(R.id.buttonLSWebSearch);
        b.setOnClickListener(v -> {
            Uri uri = null;
            try {
                uri = Uri.parse("https://www.google.com/search?q=" + URLEncoder.encode(lr.getArtist(), "utf-8") + "+" + URLEncoder.encode(lr.getTitle(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e("LYRICS_RESULT_VIEW", e.getMessage());
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(parentActivity.getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }

}