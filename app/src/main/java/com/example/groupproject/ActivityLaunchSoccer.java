package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class ActivityLaunchSoccer extends AppCompatActivity {

    Button loginButton;
    Intent goToLoading;
    EditText passField = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_match_login);

        goToLoading = new Intent(ActivityLaunchSoccer.this, Loading.class);

        loginButton = findViewById(R.id.button3);
        passField = findViewById(R.id.textView6);

        loginButton.setOnClickListener(bt -> {

            Context context = getApplicationContext();
            String text1 = "Please fill all blanks";
            String text2 = "Password is not correct";
            int duration = Toast.LENGTH_SHORT;

            if (passField.getText().toString().matches("")) {
                Toast.makeText(context, text1, duration).show();
            }
            else if (passField.getText().toString().matches("111")) {
                startActivity(goToLoading);
            } else {
                Snackbar.make(loginButton, text2, Snackbar.LENGTH_LONG).setAction("hack", click->startActivity(goToLoading)).show();
            }

        });
    }
}