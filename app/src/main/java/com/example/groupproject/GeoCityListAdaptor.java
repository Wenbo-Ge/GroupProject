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
//        boolean isSent = getItem(position).getIsSent();
//        String msgString = getItem(position).getMsg();

        LayoutInflater inflater = LayoutInflater.from(mContext);

        convertView = inflater.inflate(R.layout.activity_geo_result, parent, false);

//        Log.e(GEO_CITY_LIST_ADAPTOR, "Num of cities: " + cityList.size());
        TextView cityText = convertView.findViewById(R.id.cityText);
        cityText.setText(getItem(position).getCity());


 /*
        for(int i = 0; i < cityList.size(); i++) {
            TextView cityText = convertView.findViewById(R.id.cityText);
            Log.e(GEO_CITY_LIST_ADAPTOR, "City: " + cityList.get(i).getCity());
            cityText.setText(cityList.get(i).getCity());
        }

  */
//        TextView textField = convertView.findViewById(R.id.textFieldSend);
//        textField.setText(msgString);
        /*
        if (isSent) {
            convertView = inflater.inflate(R.layout.row_layout_2, parent, false);
            TextView textField = convertView.findViewById(R.id.textFieldSend);
            textField.setText(msgString);
        } else {
            convertView = inflater.inflate(R.layout.row_layout, parent, false);
            TextView textField = convertView.findViewById(R.id.textFieldReceive);
            textField.setText(msgString);
        }
*/
        //return it to be put in the table
        return convertView;
    }

}
