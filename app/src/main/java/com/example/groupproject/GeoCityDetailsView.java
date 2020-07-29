package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class GeoCityDetailsView extends AppCompatActivity {

    ArrayList<GeoCity> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_city_details_view);

        Bundle bundle = getIntent().getExtras();
        if(bundle.get("cityList") != null) {
            cityList = bundle.getParcelableArrayList("cityList");
        }
        if(bundle.get("favCityList") != null) {
            cityList = bundle.getParcelableArrayList("favCityList");
        }

        GeoCityDetailsFragment dFragment = new GeoCityDetailsFragment();
        dFragment.setPosition(GeoCitySearchResult.getCityIndex());
        dFragment.setArguments(cityList);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, dFragment)
                .commit();
    }
}