package com.example.groupproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class GeoCityListAdaptor extends ArrayAdapter<GeoCity> {
    private static final String GEO_CITY_LIST_ADAPTOR = "GEO_CITY_LIST_ADAPTOR";
    private ArrayList<GeoCity> cityList;
    private Context mContext;
    private int mResource;

    public GeoCityListAdaptor(@NonNull Context context, int resource, ArrayList<GeoCity> objects) {
        super(context, resource, objects);
        cityList = objects;
        mContext = context;
        mResource = resource;
    }


    @Override
    public int getCount() { return cityList.size(); }

    @Override
    public GeoCity getItem(int position) {
        return cityList.get(position);
    }
/*
    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        convertView = inflater.inflate(R.layout.activity_geo_result, parent, false);

        TextView cityText = convertView.findViewById(R.id.cityText);
        cityText.setText(getItem(position).getCity());

        //return it to be put in the table
        return convertView;
    }

}
