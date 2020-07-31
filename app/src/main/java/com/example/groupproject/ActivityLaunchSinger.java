package com.example.groupproject;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;


public class ActivityLaunchSinger extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_singer);


        Button searchButton;
        EditText searchField= null;

        Intent goToLoading = new Intent(ActivityLaunchSinger.this, ActivitySingerLoading.class);

        searchButton = findViewById(R.id.searchButton);
        searchField = findViewById(R.id.launchEdittext);


        EditText finalSearchField = searchField;
        searchButton.setOnClickListener(bt -> {
            Context context = getApplicationContext();
            String text = "Please enter a person's name";

            String text1 = "The name format is not correct!";
            int duration = Toast.LENGTH_SHORT;

            if (finalSearchField.getText().toString().matches("")) {
                Toast.makeText(context, text, duration).show();
            }
            else if (finalSearchField.getText().toString().matches("police")) {
                startActivity(goToLoading);
            } else {
                Snackbar.make(searchButton, text1, Snackbar.LENGTH_LONG).setAction("back", click->startActivity(goToLoading)).show();
            }

        });
    }


}

