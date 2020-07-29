package com.example.groupproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GeoCitySearchResult extends AppCompatActivity {
    private static final String GEO_CITY_SEARCH_RESULT = "GEO_CITY_SEARCH_RESULT";
    private GeoCityListAdaptor cityListAdaptor;
    private boolean onTablet;
    private Bundle bundle;
    private static int cityIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_result);

        ListView cityListView = findViewById(R.id.resultListView);
        bundle = getIntent().getExtras();
        ArrayList<GeoCity> cityList = bundle.getParcelableArrayList("cityList");

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
                    Intent goToFragment = new Intent (GeoCitySearchResult.this, GeoCityDetailsView.class);
                    goToFragment.putExtras(bundle);
                    startActivity(goToFragment);
                }
            }
        });
    }

    public static int getCityIndex() {
        return cityIndex;
    }
}
