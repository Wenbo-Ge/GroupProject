package com.example.groupproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GeoCitySearchResult extends AppCompatActivity {
    private static final String GEO_CITY_SEARCH_RESULT = "GEO_CITY_SEARCH_RESULT";
    private GeoCityListAdaptor cityListAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_result);

        ListView cityListView = findViewById(R.id.resultListView);
        Bundle bundle = getIntent().getExtras();
        ArrayList<GeoCity> cityList = bundle.getParcelableArrayList("cityList");

        cityListAdaptor = new GeoCityListAdaptor(this, R.layout.activity_geo_result, cityList);
        cityListView.setAdapter(cityListAdaptor);

        /*
        DetailsFragment dFragment = new DetailsFragment();
        dFragment.setArguments(data);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, dFragment)
                .commit();

 */
    }
}
