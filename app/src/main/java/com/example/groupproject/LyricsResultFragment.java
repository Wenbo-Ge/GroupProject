package com.example.groupproject;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;

public class LyricsResultFragment extends Fragment {

    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_lyrics_result, container, false);

        Bundle dataFromActivity = getArguments();
        TextView tv = result.findViewById(R.id.textViewLSTitle);
        tv.setText(dataFromActivity.getString(ActivityLaunchLyrics.TITLE));
        tv = result.findViewById(R.id.textViewLSArtist);
        tv.setText(dataFromActivity.getString(ActivityLaunchLyrics.ARTIST));
        tv = result.findViewById(R.id.textViewLSLyrics);
        tv.setText(dataFromActivity.getString(ActivityLaunchLyrics.LYRICS));
        ToggleButton favorite = result.findViewById(R.id.toggleButtonLSFavorite);
        boolean isFavorite = dataFromActivity.getBoolean(ActivityLaunchLyrics.FAVORITE, false);
        favorite.setChecked(isFavorite);
        favorite.setBackgroundDrawable(ContextCompat.getDrawable(parentActivity.getApplicationContext(), isFavorite ? R.drawable.heart_filled : R.drawable.heart_empty));
        favorite.setOnCheckedChangeListener((v, isChecked) -> {
            favorite.setBackgroundDrawable(ContextCompat.getDrawable(parentActivity.getApplicationContext(), isChecked ? R.drawable.heart_filled : R.drawable.heart_empty));
            Snackbar.make(favorite, getResources().getString(isChecked ? R.string.snackbarLSFavoriteOn : R.string.snackbarLSFavoriteOff), Snackbar.LENGTH_SHORT)
                    .setAction(getResources().getString(R.string.snackbarLSFavoriteUndo),click -> favorite.setChecked(!isChecked)).show();
        });
        Button b = result.findViewById(R.id.buttonLSWebSearch);
        b.setOnClickListener(v -> {

        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }
}