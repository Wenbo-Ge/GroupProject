package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GeoFavCityDetailsView extends AppCompatActivity {

    ArrayList<GeoCity> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fav_city_details_view);

        Bundle bundle = getIntent().getExtras();
        if(bundle.get("cityList") != null) {
            cityList = bundle.getParcelableArrayList("cityList");
        }
        if(bundle.get("favCityList") != null) {
            cityList = bundle.getParcelableArrayList("favCityList");
        }

        GeoFavCityDetailsFragment dFragment = new GeoFavCityDetailsFragment();
        dFragment.setPosition(GeoCitySearchResult.getCityIndex());
        dFragment.setArguments(cityList);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, dFragment)
                .commit();

    }
}