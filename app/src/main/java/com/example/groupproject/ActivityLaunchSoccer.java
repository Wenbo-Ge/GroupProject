package com.example.groupproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class ActivityLaunchSoccer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button loginButton;
    Intent goToLoading;
    EditText passField = null;
    SharedPreferences prefs = null;
    Toolbar tBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_soccer);

        tBar = (Toolbar)findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);

        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.soccer_open, R.string.soccer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //launch loading page

        goToLoading = new Intent(ActivityLaunchSoccer.this, SoccerLoading.class);

        prefs = getSharedPreferences("SavedPassword", Context.MODE_PRIVATE);

        String savedString = prefs.getString("Pass", "");

        loginButton = findViewById(R.id.button3);
        passField = findViewById(R.id.textView6);
        passField.setText(savedString);

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

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPrefs(passField.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the soccer_menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.soccer_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your soccer_menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the soccer_menu item is selected:
            case R.id.soccerMap:
                startActivity(new Intent(ActivityLaunchSoccer.this, ActivityLaunchGeo.class));
                break;
            case R.id.soccerSong:
                startActivity(new Intent(ActivityLaunchSoccer.this, ActivityLaunchSinger.class));
                break;
            case R.id.soccerLyrics:
                startActivity(new Intent(ActivityLaunchSoccer.this, ActivityLaunchLyrics.class));
                break;
            case R.id.soccerInfoField:
                message = "This is the Soccer activity, written by Wenbo Ge";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.soccerHeaderInstruction:
                AlertDialog.Builder builderInstruction = new AlertDialog.Builder(this);
                builderInstruction.setTitle("Instruction")
                        .setMessage("Please first login \n" +
                                "Select the match you are interested \n" +
                                "You can click 'Watch your match' to checked saved matches") //add the 3 edit texts showing the contact information
                        .setNegativeButton("dismiss", (click, b) -> { })
                        .create().show();
                break;
            case R.id.soccerHeaderAPI:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.scorebat.com/video-api/v1/")));
                break;
            case R.id.soccerHeaderDonate:
                AlertDialog.Builder builderDonate = new AlertDialog.Builder(this);
                builderDonate.setView(getLayoutInflater().inflate(R.layout.activity_soccer_donate, null))
                        .setNegativeButton("Cancel", (click, b) -> { })
                        .setPositiveButton("Thank you", (click, b) -> { })
                        .create().show();
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void saveSharedPrefs(String stringToSave) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Pass", stringToSave);
        editor.commit();
    }
}