package com.example.groupproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.pm.PackageManager.*;

public class GeoFavCityDetailsFragment extends Fragment {

    private static final String GEO_FAV_CITY_FRAGMENT = "GEO_FAV_CITY_FRAGMENT";
    private ArrayList<GeoCity> cityList;
    private int index;
    private SQLiteDatabase db;
    private String lat, lng;

    public void setArguments(ArrayList<GeoCity> cityList) {
        this.cityList = cityList;
    }

    public void setPosition(int index) {
        this.index = index;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_geo_fav_city_details, container, false);

        loadDataFromDatabase();

        TextView country = (TextView)result.findViewById(R.id.country);
        country.setText(cityList.get(index).getCountry());

        TextView region = (TextView)result.findViewById(R.id.region);
        region.setText(cityList.get(index).getRegion());

        TextView city = (TextView)result.findViewById(R.id.city);
        city.setText(cityList.get(index).getCity());

        TextView latitude = (TextView)result.findViewById(R.id.latitude);
        lat = cityList.get(index).getLatitude();
        latitude.setText(lat);

        TextView longitude = (TextView)result.findViewById(R.id.longitude);
        lng = cityList.get(index).getLongitude();
        longitude.setText(lng);

        TextView currency = (TextView)result.findViewById(R.id.currency);
        currency.setText(cityList.get(index).getCurrency());

        Button rmButton = (Button)result.findViewById(R.id.rmFromFav);
        rmButton.setOnClickListener(bt -> {
            openDB();

            Log.i(GEO_FAV_CITY_FRAGMENT, "index: " + index);
            Log.i(GEO_FAV_CITY_FRAGMENT, "_id: " + cityList.get(index).getId());
            long newId = db.delete(GeoCityDBOpener.TABLE_NAME, "_id=" + cityList.get(index).getId(), null);
            cityList.remove(index);
            Log.i(GEO_FAV_CITY_FRAGMENT, "row affected: " + newId);
//            GeoCitySearchResult.cityListAdaptor.notifyDataSetChanged();
            Toast.makeText(getContext(), R.string.geoDeleted, Toast.LENGTH_SHORT).show();
        });

        return result;
    }

    private void loadDataFromDatabase() {
        cityList.clear();
        GeoCityDBOpener dbOpener = new GeoCityDBOpener(getContext());
        db = dbOpener.getWritableDatabase();

        // Get all the columns of the table
        String [] columns = {GeoCityDBOpener.COL_ID, GeoCityDBOpener.COL_COUNTRY,
                GeoCityDBOpener.COL_REGION, GeoCityDBOpener.COL_CITY, GeoCityDBOpener.COL_LATITUDE,
                GeoCityDBOpener.COL_LONGITUDE, GeoCityDBOpener.COL_CURRENCY};
        // Query all the results from the table
        Cursor results = db.query(false, dbOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        printCursor(results, db.getVersion());

        int idColIndex = results.getColumnIndex(dbOpener.COL_ID);
        int countryColIndex = results.getColumnIndex(dbOpener.COL_COUNTRY);
        int regionColIndex = results.getColumnIndex(dbOpener.COL_REGION);
        int cityColIndex = results.getColumnIndex(dbOpener.COL_CITY);
        int latColIndex = results.getColumnIndex(dbOpener.COL_LATITUDE);
        int lngColIndex = results.getColumnIndex(dbOpener.COL_LONGITUDE);
        int currencyColIndex = results.getColumnIndex(dbOpener.COL_CURRENCY);

        if(results.isFirst()) {
            do {
                long id = results.getLong(idColIndex);
                String country = results.getString(countryColIndex);
                String region = results.getString(regionColIndex);
                String city = results.getString(cityColIndex);
                String lat = results.getString(latColIndex);
                String lng = results.getString(lngColIndex);
                String currency = results.getString(currencyColIndex);

                cityList.add(new GeoCity(id, country, region, city, lat, lng, currency));
            } while (results.moveToNext());
        }

        Log.i(GEO_FAV_CITY_FRAGMENT, "size is: " + cityList.size());
        for(int i = 0; i < cityList.size() ; i++) {
            Log.i(GEO_FAV_CITY_FRAGMENT, "" +
                    cityList.get(i).getId() + " " +
                    cityList.get(i).getCountry() + " " +
                    cityList.get(i).getRegion() + " " +
                    cityList.get(i).getCity() + " " +
                    cityList.get(i).getLatitude() + " " +
                    cityList.get(i).getLongitude() + " " +
                    cityList.get(i).getCurrency());
        }
    }

    public void openDB() {
        GeoCityDBOpener dbOpener = new GeoCityDBOpener(getContext());
        db = dbOpener.getWritableDatabase();

        // Get all the columns of the table
        String [] columns = {GeoCityDBOpener.COL_ID, GeoCityDBOpener.COL_COUNTRY,
                GeoCityDBOpener.COL_REGION, GeoCityDBOpener.COL_CITY, GeoCityDBOpener.COL_LATITUDE,
                GeoCityDBOpener.COL_LONGITUDE, GeoCityDBOpener.COL_CURRENCY};
        // Query all the results from the table
        Cursor results = db.query(false, GeoCityDBOpener.TABLE_NAME, columns, null, null, null, null, null, null);
//        printCursor(results, db.getVersion());
    }

    private void printCursor(Cursor c, int version) {
        Log.e(GEO_FAV_CITY_FRAGMENT, "In printCursor");
        Log.e(GEO_FAV_CITY_FRAGMENT, "database version: " + version);
        Log.e(GEO_FAV_CITY_FRAGMENT, "num of cols: " + c.getColumnCount());
        Log.e(GEO_FAV_CITY_FRAGMENT, "num of rows: " + c.getCount());

        int colCount = c.getColumnCount();

        // Print out column names
        String [] colNames = c.getColumnNames();
        String nameRow = new String();
        for (int i = 0; i < colCount; i++) {
            nameRow = nameRow.concat(colNames[i] + "\t");
        }
        Log.e(GEO_FAV_CITY_FRAGMENT, nameRow);

        // Print out each row in the table
        while(c.moveToNext()) {
            String contentRow = new String();
            for (int i = 0; i < colCount; i++) {
                contentRow = contentRow.concat(c.getString(i) + "\t");
            }
            Log.e(GEO_FAV_CITY_FRAGMENT, contentRow);
        }

        c.moveToFirst();
    }
}