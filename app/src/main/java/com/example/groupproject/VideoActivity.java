package com.example.groupproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class VideoActivity extends AppCompatActivity {
    MyListAdapter myAdapter;
    SQLiteDatabase db;
    ListView myList;
    MyOpener dbOpener;
    Cursor results;
    Button refreshButton;
    Button watchButton;
    Button savedButton;

    private ArrayList<Video> elements = new ArrayList<>( Arrays.asList());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_match_highlights);



        myList =  findViewById(R.id.theListView);

        loadDataFromDatabase();

        myList.setAdapter( myAdapter = new MyListAdapter() );

        refreshButton = findViewById(R.id.button2);

        savedButton = findViewById(R.id.button3);

        watchButton = findViewById(R.id.watch);

        refreshButton.setOnClickListener( click -> {
            ContentValues newRowValues = new ContentValues();

            newRowValues.put(MyOpener.COL_COUNTRY, "ITALY: Serie A");
            newRowValues.put(MyOpener.COL_DATE, "2020-07-09T17:00");
            newRowValues.put(MyOpener.COL_SIDE1, "AC Milan");
            newRowValues.put(MyOpener.COL_SIDE2, "Juventus");
            newRowValues.put(MyOpener.COL_EMBED, "https:\\/\\/www.scorebat.com\\/ac-milan-vs-juventus-live-stream\\/");
            newRowValues.put(MyOpener.COL_LIKE, 0);


            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
            Video video = new Video("ITALY: Serie A", "2020-07-09T17:00", "AC Milan", "Juventus", "https:\\/\\/www.scorebat.com\\/ac-milan-vs-juventus-live-stream\\/", false, newId);

            elements.add(video);
            myAdapter.notifyDataSetChanged();
        });

//        savedButton.setOnClickListener( click -> {
//        });

//        watchButton.setOnClickListener( click -> {
//
//        });


        myList.setOnItemLongClickListener( (parent, view, pos, id) -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this?")

                    //What is the message:
                    .setMessage("The selected row is" + (pos+1) + "\n The database id is: " + 1)

                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {
                        deleteVideo(myAdapter.getItem(pos));
                        elements.remove(myAdapter.getItem(pos));
                        myAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg) -> { })
                    //Show the dialog
                    .create().show();

//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("You clicked on item #" + position)
//                    .setMessage("You can update the fields and then click update to save in the database")
//                    .setView(contact_view) //add the 3 edit texts showing the contact information
//                    .setPositiveButton("Update", (click, b) -> {
//                        selectedContact.update(rowName.getText().toString(), rowEmail.getText().toString());
//                        updateContact(selectedContact);
//                        myAdapter.notifyDataSetChanged(); //the email and name have changed so rebuild the list
//                    })
//                    .setNegativeButton("Delete", (click, b) -> {
//                        deleteContact(selectedContact); //remove the contact from database
//                        contactsList.remove(position); //remove the contact from contact list
//                        myAdapter.notifyDataSetChanged(); //there is one less item so update the list
//                    })
//                    .setNeutralButton("dismiss", (click, b) -> { })
//                    .create().show();
            return true;
        });

    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();
        startActivity(new Intent(VideoActivity.this, ActivityLaunchSoccer.class));
        finish();
    }


    private class MyListAdapter extends BaseAdapter {

        public int getCount() { return elements.size();}

        public Video getItem(int position) { return elements.get(position); }

        public long getItemId(int position) { return getItem(position).getId(); }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            Video video = getItem(position);
            TextView countryView;
            TextView dateView;
            TextView side1View;
            TextView side2View;


            convertView = inflater.inflate(R.layout.activity_row_layout, parent, false);

            countryView = convertView.findViewById(R.id.country);
            dateView = convertView.findViewById(R.id.date);
            side1View = convertView.findViewById(R.id.textViewA);
            side2View = convertView.findViewById(R.id.textViewB);

            countryView.setText(video.getCountry());
            dateView.setText(video.getDate());
            side1View.setText(video.getSide1());
            side2View.setText(video.getSide2());
            return convertView;
        }
    }

    private void loadDataFromDatabase()
    {

        dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();


        String [] columns = {MyOpener.COL_ID, MyOpener.COL_COUNTRY, MyOpener.COL_DATE, MyOpener.COL_SIDE1, MyOpener.COL_SIDE2, MyOpener.COL_EMBED};

        results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);


        int countryColumnIndex = results.getColumnIndex(MyOpener.COL_COUNTRY);
        int dateColumnIndex = results.getColumnIndex(MyOpener.COL_DATE);
        int side1ColumnIndex = results.getColumnIndex(MyOpener.COL_SIDE1);
        int side2ColumnIndex = results.getColumnIndex(MyOpener.COL_SIDE2);
        int embedColumnIndex = results.getColumnIndex(MyOpener.COL_EMBED);
        int likeColumnIndex = results.getColumnIndex(MyOpener.COL_LIKE);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);


        while(results.moveToNext())
        {
            String country = results.getString(countryColumnIndex);
            String date = results.getString(dateColumnIndex);
            String side1 = results.getString(side1ColumnIndex);
            String side2 = results.getString(side2ColumnIndex);
            String embed = results.getString(embedColumnIndex);
            boolean liked = results.getInt(likeColumnIndex) == 1;
            long id = results.getLong(idColIndex);


            elements.add(new Video(country, date, side1, side2, embed, liked, id));
//            printCursor(results,db.getVersion());

        }

    }

    protected void deleteVideo(Video m)
    {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(m.getId())});
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
