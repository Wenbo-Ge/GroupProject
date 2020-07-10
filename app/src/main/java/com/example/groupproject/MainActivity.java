package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    ImageButton geoButton;
    ImageButton soccerButton;
    ImageButton lyricsButton;
    ImageButton singerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        geoButton = findViewById(R.id.imageGeo);
        soccerButton = findViewById(R.id.imageSoccer);
        lyricsButton = findViewById(R.id.imageSong);
        singerButton = findViewById(R.id.imageDeezer);

        geoButton.setOnClickListener(bt -> {
            startActivity(new Intent(MainActivity.this, ActivityLaunchGeo.class));
        });

        soccerButton.setOnClickListener(bt -> {
            startActivity(new Intent(MainActivity.this, ActivityLaunchSoccer.class));
        });

        lyricsButton.setOnClickListener(bt -> {
            startActivity(new Intent(MainActivity.this, ActivityLaunchLyrics.class));
        });

        singerButton.setOnClickListener(bt -> {
            startActivity(new Intent(MainActivity.this, ActivityLaunchSinger.class));
        });

    }
}