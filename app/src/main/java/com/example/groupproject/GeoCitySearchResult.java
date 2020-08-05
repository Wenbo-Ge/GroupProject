package com.example.groupproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class GeoCitySearchResult extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String GEO_CITY_SEARCH_RESULT = "GEO_CITY_SEARCH_RESULT";
    private GeoCityListAdaptor cityListAdaptor;
    private ArrayList<GeoCity> cityList;
    private boolean onTablet;
    private boolean isFavCityView;
    private Bundle bundle;
    private static int cityIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_result);

        Toolbar tBar = (Toolbar)findViewById(R.id.geo_toolbar);
        setSupportActionBar(tBar);
/*
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.geoNavDrawerOpen, R.string.geoNavDrawerClose);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
*/
        ListView cityListView = findViewById(R.id.resultListView);
        bundle = getIntent().getExtras();

        if(bundle.get("cityList") != null) {
            isFavCityView = false;
            cityList = bundle.getParcelableArrayList("cityList");
        }
        if(bundle.get("favCityList") != null) {
            isFavCityView = true;
            cityList = bundle.getParcelableArrayList("favCityList");
        }

        cityListAdaptor = new GeoCityListAdaptor(this, R.layout.activity_geo_result, cityList);
        cityListView.setAdapter(cityListAdaptor);

        FrameLayout frameLayout = findViewById(R.id.fragmentLocation);
        if(frameLayout == null) {
            onTablet = false;
        } else {
            onTablet = true;
        }

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(GEO_CITY_SEARCH_RESULT, "Item clicked: " + i);

                if(onTablet) {
                    Log.i(GEO_CITY_SEARCH_RESULT, "On tablet");
                    GeoCityDetailsFragment dFragment = new GeoCityDetailsFragment();
                    dFragment.setPosition(i);
                    dFragment.setArguments(cityList);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentLocation, dFragment)
                            .commit();
                }
                else {
                    Log.i(GEO_CITY_SEARCH_RESULT, "On phone");
                    cityIndex = i;
                    Intent goToFragment;
                    if(!isFavCityView) {
                        goToFragment = new Intent(GeoCitySearchResult.this, GeoCityDetailsView.class);
                    }
                    else {
                        goToFragment = new Intent(GeoCitySearchResult.this, GeoFavCityDetailsView.class);
                    }
                    goToFragment.putExtras(bundle);
                    startActivity(goToFragment);
                }
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (isFavCityView) {
            openDB();
            cityListAdaptor.notifyDataSetChanged();
        }
    }

    public static int getCityIndex() {
        return cityIndex;
    }

    public void openDB() {
        GeoCityDBOpener dbOpener = new GeoCityDBOpener(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        // Get all the columns of the table
        String [] columns = {GeoCityDBOpener.COL_ID, GeoCityDBOpener.COL_COUNTRY,
                GeoCityDBOpener.COL_REGION, GeoCityDBOpener.COL_CITY, GeoCityDBOpener.COL_LATITUDE,
                GeoCityDBOpener.COL_LONGITUDE, GeoCityDBOpener.COL_CURRENCY};
        // Query all the results from the table
        Cursor results = db.query(false, GeoCityDBOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        cityList.clear();
        while(results.moveToNext()) {

            cityList.add(new GeoCity(
                    results.getLong(results.getColumnIndex(dbOpener.COL_ID)),
                    results.getString(results.getColumnIndex(dbOpener.COL_COUNTRY)),
                    results.getString(results.getColumnIndex(dbOpener.COL_REGION)),
                    results.getString(results.getColumnIndex(dbOpener.COL_CITY)),
                    results.getString(results.getColumnIndex(dbOpener.COL_LATITUDE)),
                    results.getString(results.getColumnIndex(dbOpener.COL_LONGITUDE)),
                    results.getString(results.getColumnIndex(dbOpener.COL_CURRENCY))));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.geo_toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent goToIntent = null;

        switch(item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.menuAbout:
                Toast.makeText(this, R.string.geoAboutProject, Toast.LENGTH_LONG).show();
                break;
            case R.id.menuDeezer:
                goToIntent = new Intent(GeoCitySearchResult.this, ActivityLaunchLyrics.class);
                break;
            case R.id.menuSong:
                goToIntent = new Intent(GeoCitySearchResult.this, ActivityLaunchLyrics.class);
                break;
            case R.id.menuSoccer:
                goToIntent = new Intent(GeoCitySearchResult.this, ActivityLaunchSoccer.class);
                break;
        }

        if(goToIntent != null) {
            startActivity(goToIntent);
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
        switch(item.getItemId()) {
            case R.id.geoNavInstructions:
                alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getString(R.string.geoInstructionTitle))
                        .setMessage(getString(R.string.geoInstructionDetails))
                        .setPositiveButton(getString(R.string.geoBackBtn), (click, arg) -> {})
                        .create().show();
                break;
            case R.id.geoNavAboutAPI:
                Uri uri = Uri.parse("https://www.geodatasource.com/web-service");

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(this.getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.geoNavDonate:
                alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
                View donateView = getLayoutInflater().inflate(R.layout.activity_geo_donate, null);
                alertDialogBuilder.setView(donateView);
                alertDialogBuilder.setTitle(getString(R.string.geoDonateTitle))
                        .setMessage(getString(R.string.geoDonateDetails))
                        .setPositiveButton(getString(R.string.geoThankYouBtn), (click, arg) -> {})
                        .setNegativeButton(getString(R.string.geoCancelBtn), (click, arg) -> { })
                        .create().show();
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }
}
