
package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

public class ActivityLaunchGeo extends AppCompatActivity {
    private static final String ACTIVITY_LAUNCH_GEO = "ACTIVITY_LAUNCH_GEO";
    EditText latField, lonField;
    Button searchBtn, showFavBtn, helpBtn;
    ProgressBar pBar;
    ArrayList<GeoCity> cityList;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_geo);

        latField = findViewById(R.id.latitudeField);
        lonField = findViewById(R.id.longitudeField);
        searchBtn = findViewById(R.id.searchButton);
        showFavBtn = findViewById(R.id.showFavoritesButton);
        pBar = findViewById(R.id.progressBar);
        helpBtn = findViewById(R.id.helpButton);

        cityList = new ArrayList<>();
        bundle = new Bundle();

        searchBtn.setOnClickListener(bt -> {
            String lat = latField.getText().toString();
            String lng = lonField.getText().toString();

            if(!lat.isEmpty() && !lng.isEmpty()) {
                String urlStr = new String("https://api.geodatasource.com/cities?key=BIWYTHEHQX0UOENOSAL1EOCF2VCSFHBW&lat="
                        + lat + "&lng=" + lng + "&format=xml");
                Log.i(ACTIVITY_LAUNCH_GEO, "urlStr: " + urlStr);
                CitySearchQuery query = new CitySearchQuery();
                query.execute(urlStr);
            }
        });

        helpBtn.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLaunchGeo.this);
                builder.setTitle(R.string.geoTips)
                        .setMessage(R.string.geoHelpMsg)
                        .setCancelable(false)
                        .setNegativeButton(R.string.geoNavBtn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), R.string.geoBack, Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    protected class CitySearchQuery extends AsyncTask<String, Integer, String> {
        private String country, region, city, currency, latitude, longitude;
        private int progress = 0, increment = 5;

        public String doInBackground(String... args) {
            Log.i(ACTIVITY_LAUNCH_GEO, "Start background process");
            try {
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT
                boolean newCity = false;
//                pBar.setVisibility(View.VISIBLE);

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
//                        Log.i(ACTIVITY_LAUNCH_GEO, "Tag: " + xpp.getName());
                        if (xpp.getName().equals("country")) {
                            country = new String(xpp.nextText());
//                          Log.i(ACTIVITY_LAUNCH_GEO, "country: " + country);
                        } else if (xpp.getName().equals("region")) {
                            region = new String(xpp.nextText());
//                          Log.i(ACTIVITY_LAUNCH_GEO, "region: " + region);
                        } else if (xpp.getName().equals("city")) {
                            //If you get here, then you are pointing to a <city> start tag
                            city = new String(xpp.nextText());
//                          Log.i(ACTIVITY_LAUNCH_GEO, "city: " + city);
                        } else if (xpp.getName().equals("latitude")) {
                            latitude = new String(xpp.nextText());
//                          Log.i(ACTIVITY_LAUNCH_GEO, "latitude: " + latitude);
                        } else if (xpp.getName().equals("longitude")) {
                            longitude = new String(xpp.nextText());
//                          Log.i(ACTIVITY_LAUNCH_GEO, "longitude: " + longitude);
                        } else if (xpp.getName().equals("currency_name")) {
                            currency = new String(xpp.nextText());
                            newCity = true;
//                          Log.i(ACTIVITY_LAUNCH_GEO, "currency: " + currency);
                        }
                    }

                    if(newCity) {
                        cityList.add(new GeoCity(country, region, city, currency, latitude, longitude));
                        newCity = false;

                        if(progress < 100 - increment) {
                            progress += increment;
                            publishProgress(progress);
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

                publishProgress(100);

                Log.i(ACTIVITY_LAUNCH_GEO, "Num of cities: " + cityList.size());
                for(int i = 0; i < cityList.size(); i++) {
                    Log.i(ACTIVITY_LAUNCH_GEO, "City: " + cityList.get(i).getCity());
                }

            } catch (Exception e) {

            }
            return "Done";
        }

        public void onProgressUpdate(Integer... value) {
            Log.i(ACTIVITY_LAUNCH_GEO, "progress: " + value[0]);
            pBar.setVisibility(View.VISIBLE);
            pBar.setProgress(value[0]);
        }

        public void onPostExecute(String fromDoingInBackground) {
            Log.i(ACTIVITY_LAUNCH_GEO, fromDoingInBackground);
            pBar.setVisibility(View.INVISIBLE);

            bundle.putParcelableArrayList("cityList", cityList);
            Intent goToCitySearchResult = new Intent(ActivityLaunchGeo.this, GeoCitySearchResult.class);
            goToCitySearchResult.putExtras(bundle);
            startActivity(goToCitySearchResult);
        }
    }

    private void showCitySearchResult(String apiStr) {
        try {
            URL url = new URL(apiStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;

            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pBar.setVisibility(View.INVISIBLE);
    }
}