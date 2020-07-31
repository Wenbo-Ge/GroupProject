package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class GeoCityDetailsView extends AppCompatActivity {

    ArrayList<GeoCity> cityList;
    private boolean isFavCityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_city_details_view);

        Bundle bundle = getIntent().getExtras();
        if(bundle.get("cityList") != null) {
            isFavCityView = false;
            cityList = bundle.getParcelableArrayList("cityList");

            GeoCityDetailsFragment dFragment = new GeoCityDetailsFragment();
            dFragment.setPosition(GeoCitySearchResult.getCityIndex());
            dFragment.setArguments(cityList);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, dFragment)
                    .commit();
        }
        if(bundle.get("favCityList") != null) {
            isFavCityView = true;
            cityList = bundle.getParcelableArrayList("favCityList");

            GeoFavCityDetailsFragment dFragment = new GeoFavCityDetailsFragment();
        }
    }
}