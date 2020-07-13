package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;

public class LyricsResultActivity extends AppCompatActivity {

    private ToggleButton favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_result);
        Intent from = getIntent();
        TextView tv = findViewById(R.id.textViewLSArtist);
        tv.setText(from.getStringExtra("ARTIST"));
        tv = findViewById(R.id.textViewLSTitle);
        tv.setText(from.getStringExtra("TITLE"));
        tv = findViewById(R.id.textViewLSLyrics);
        tv.setText(from.getStringExtra("LYRICS"));
        favorite = (ToggleButton) findViewById(R.id.toggleButtonLSFavorite);
        boolean isFavorite = from.getBooleanExtra("FAVORITE", false);
        favorite.setChecked(isFavorite);
        favorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), isFavorite ? R.drawable.heart_filled : R.drawable.heart_empty));
        favorite.setOnCheckedChangeListener((v, isChecked) -> {
                favorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), isChecked ? R.drawable.heart_filled : R.drawable.heart_empty));
                Snackbar.make(favorite, getResources().getString(isChecked ? R.string.snackbarLSFavoriteOn : R.string.snackbarLSFavoriteOff), Snackbar.LENGTH_SHORT)
                    .setAction(getResources().getString(R.string.snackbarLSFavoriteUndo),click -> favorite.setChecked(!isChecked)).show();
            });
    }
}