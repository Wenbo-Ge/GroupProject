package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;

public class LyricsResultActivity extends AppCompatActivity {

    private Intent returnIntent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_result);
        Bundle dataToPass = getIntent().getExtras();

        LyricsResultFragment fragment = new LyricsResultFragment(); //add a DetailFragment
        fragment.setArguments( dataToPass ); //pass it a bundle for information
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, fragment) //Add the fragment in FrameLayout
                .commit(); //actually load the fragment. Calls onCreate() in DetailFragment


        Toolbar tBar = (Toolbar)findViewById(R.id.toolbarLSResult);
        setSupportActionBar(tBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lyrics_search_menu, menu);
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
                goTo = new Intent(LyricsResultActivity.this, ActivityLaunchSinger.class);
                break;
            case R.id.menuLSgeo:
                goTo = new Intent(LyricsResultActivity.this, ActivityLaunchGeo.class);
                break;
            case R.id.menuLSsoccer:
                goTo = new Intent(LyricsResultActivity.this, ActivityLaunchSoccer.class);
                break;
        }
        if (goTo != null) startActivity(goTo);
        return true;
    }

}