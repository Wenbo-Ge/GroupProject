package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;

public class LyricsResultActivity extends AppCompatActivity {

    private ToggleButton favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_result);
        favorite = (ToggleButton) findViewById(R.id.toggleButtonLSFavorite);
        favorite.setChecked(false);
        favorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.heart_empty));
        favorite.setOnCheckedChangeListener((v, isChecked) -> {
                favorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), isChecked ? R.drawable.heart_filled : R.drawable.heart_empty));
                Snackbar.make(favorite, getResources().getString(isChecked ? R.string.snackbarLSFavoriteOn : R.string.snackbarLSFavoriteOff), Snackbar.LENGTH_SHORT)
                    .setAction(getResources().getString(R.string.snackbarLSFavoriteUndo),click -> favorite.setChecked(!isChecked)).show();
            });
    }
}