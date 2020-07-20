package com.example.groupproject;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class SoccerVideoActivity extends AppCompatActivity {
    MyListAdapter myAdapter;
    SoccerMyOpener dbOpener;
    SQLiteDatabase db;
    ListView myList;

    Button yourMatchButton;

    private ArrayList<SoccerVideo> elements = new ArrayList<>( Arrays.asList());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_match_highlights);

        matchQuery req = new matchQuery();
        req.execute();

        dbOpener = new SoccerMyOpener(this);
        db = dbOpener.getWritableDatabase();

        myList =  findViewById(R.id.theListView);

        myList.setAdapter( myAdapter = new MyListAdapter() );

        SoccerVideo soccerVideo = new SoccerVideo("ITALY: Serie A", "Napoli - AC Milian","2020-07-09T17:00", "AC Milan", "Juventus", "https:\\/\\/www.scorebat.com\\/ac-milan-vs-juventus-live-stream\\/");

        elements.add(soccerVideo);

        myAdapter.notifyDataSetChanged();

        yourMatchButton = findViewById(R.id.matches);


        yourMatchButton.setOnClickListener(bt -> {
            startActivity(new Intent(SoccerVideoActivity.this, SoccerSavedMatches.class));
        });


//        watchButton.setOnClickListener( click -> {
//
//        });


        myList.setOnItemClickListener( (parent, view, pos, id) -> {
            showDetail( pos );
        });




    }

    protected void showDetail(int pos)
    {
        SoccerVideo selectedMatch = elements.get(pos);

        View detail_view = getLayoutInflater().inflate(R.layout.activity_soccer_detail, null);
        //get the TextViews


        TextView countryView = detail_view.findViewById(R.id.country);
        TextView dateView = detail_view.findViewById(R.id.date);
        TextView side1View = detail_view.findViewById(R.id.textViewA);
        TextView side2View = detail_view.findViewById(R.id.textViewB);

        //set the fields for the alert dialog
        countryView.setText(selectedMatch.getCountry());
        dateView.setText(selectedMatch.getDate());
        side1View.setText(selectedMatch.getSide1());
        side2View.setText(selectedMatch.getSide2());



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Match Details")
                .setView(detail_view) //add the 3 edit texts showing the contact information
                .setNegativeButton("dismiss", (click, b) -> { })
                .setPositiveButton("save", (click, b) -> {
                    ContentValues newRowValues = new ContentValues();

                    newRowValues.put(SoccerMyOpener.COL_COUNTRY, "ITALY: Serie A");
                    newRowValues.put(SoccerMyOpener.COL_TITLE, "Napoli - AC Milian");
                    newRowValues.put(SoccerMyOpener.COL_DATE, "2020-07-09T17:00");
                    newRowValues.put(SoccerMyOpener.COL_SIDE1, "AC Milan");
                    newRowValues.put(SoccerMyOpener.COL_SIDE2, "Juventus");
                    newRowValues.put(SoccerMyOpener.COL_EMBED, "<div style='width:100%;height:0px;position:relative;padding-bottom:calc(56.25% + 335px);' class='_scorebatEmbeddedPlayerW_'><iframe src='https:\\/\\/www.scorebat.com\\/embed\\/g\\/821217\\/?s=2' frameborder='0' width='560' height='650' allowfullscreen allow='autoplay; fullscreen' style='width:100%;height:100%;position:absolute;left:0px;top:0px;overflow:hidden;' class='_scorebatEmbeddedPlayer_'><\\/iframe><\\/div>");

                   db.insert(SoccerMyOpener.TABLE_NAME, null, newRowValues);

                })
                .setNeutralButton("watch highlight", (click, b) -> {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.scorebat.com/embed/g/821217/?s=2")));
                })
//                .setNeutralButton("dismiss", (click, b) -> { })
                .create().show();
    }

    private class matchQuery extends AsyncTask< String, Integer, String>
    {

        String side1, side2, country, matchDate, videoUrl;

        //Type3                Type1
        protected String doInBackground(String ... args)
        {
            try {

                //create a URL object of what server to contact:

                URL matchUrl= new URL("https://www.scorebat.com/video-api/v1/");


                //open the connection
                HttpURLConnection urlMatchConnection = (HttpURLConnection) matchUrl.openConnection();

                //wait for data:
                InputStream responseMatch = urlMatchConnection.getInputStream();


                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(responseMatch, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


//                 convert string to JSON: Look at slide 27:
                JSONObject matchJson = new JSONObject(result);

//                get the double associated with "value"
                side1 = matchJson.getString("side1");
                side2 = matchJson.getString("side2");
                country = matchJson.getString("competition");
                matchDate = matchJson.getString("date");
                videoUrl = matchJson.getString("url");

//                SoccerVideo soccerVideo = new SoccerVideo("ITALY: Serie A", "2020-07-09T17:00", "AC Milan", "Juventus", "https:\\/\\/www.scorebat.com\\/ac-milan-vs-juventus-live-stream\\/");
//
//                elements.add(soccerVideo);
//
//                myAdapter.notifyDataSetChanged();




            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return "Done";
        }

        //Type 2


        public void onProgressUpdate(Integer ... value)
        {

        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
//            myList.setAdapter( myAdapter = new MyListAdapter() );

        }
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();
        startActivity(new Intent(SoccerVideoActivity.this, ActivityLaunchSoccer.class));
        finish();
    }


    private class MyListAdapter extends BaseAdapter {

        public int getCount() { return elements.size();}

        public SoccerVideo getItem(int position) { return elements.get(position); }

        public long getItemId(int position) { return getItem(position).getId(); }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            SoccerVideo soccerVideo = getItem(position);

            TextView matchTitle;


            convertView = inflater.inflate(R.layout.activity_soccer_row_layout, parent, false);


            matchTitle = convertView.findViewById(R.id.matchTitle);


            matchTitle.setText(soccerVideo.getTitle());
            return convertView;
        }
    }




//    protected void printCursor (Cursor c, int version) {
//        int messageColumnIndex = c.getColumnIndex(MyOpener.COL_MESSAGE);
//        int idColIndex = c.getColumnIndex(MyOpener.COL_ID);
//        int sentColIndex = c.getColumnIndex(MyOpener.COL_SENT);
//
//        String message = c.getString(messageColumnIndex);
//        long id = c.getLong(idColIndex);
//        boolean sent = c.getInt(sentColIndex) == 1;
//        Log.i("Version Number is: ", Integer.toString(version));
//        Log.i("Number of columns: ", Integer.toString(c.getColumnCount()));
//        Log.i("Name of columns: ", Arrays.toString(c.getColumnNames()));
//        Log.i("Number of rows: ", Integer.toString(c.getCount()));
//        Log.i("Results of row: ", MyOpener.COL_ID + " " +id + " " + MyOpener.COL_MESSAGE +" " + message
//                + " " + MyOpener.COL_SENT + " " +sent);
//
//    }
}
