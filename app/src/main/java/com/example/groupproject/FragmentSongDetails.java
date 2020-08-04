package com.example.groupproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.FileOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSongDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSongDetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String tittleContent;
    private String durationCol;
    private String albumNameCol;
    private String albumCoverCol;
    private Bundle dataFromActivity;
    private String mParam1;
    private String mParam2;
    private SQLiteDatabase db;


    public FragmentSongDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSongDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSongDetails newInstance(String param1, String param2) {
        FragmentSongDetails fragment = new FragmentSongDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle dataFromActivity = getArguments();
        String tittleContent = dataFromActivity.getString(MyDatabaseOpenHelper.COL_TITLE);
        String durationCol = dataFromActivity.getString(MyDatabaseOpenHelper.COL_DURATION);
        String albumNameCol = dataFromActivity.getString(MyDatabaseOpenHelper.COL_ALBUMNAME);
        String albumCoverCol = dataFromActivity.getString(MyDatabaseOpenHelper.COL_ALBUMCOVER);

        View result = inflater.inflate(R.layout.fragment_song_details, container, false);

        TextView tittle = (TextView) result.findViewById(R.id.title);
        tittle.setText(tittleContent);

        TextView duration = (TextView) result.findViewById(R.id.duration);
        duration.setText("DURATION: " + durationCol);

        TextView albumName = (TextView) result.findViewById(R.id.albumName);
        albumName.setText("ALBUMNAME: " + albumNameCol);

        ImageView coverImage = result.findViewById(R.id.albumCover);
        Log.i("URL", albumCoverCol);


//       coverImage.setImageURI(Uri.parse(albumCoverCol));
//        coverImage.setImageBitmap(image);;



        Button saveButton = (Button) result.findViewById(R.id.saveButton);
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                ContentValues newRowValues = new ContentValues();
//
//                newRowValues.put(MyDatabaseOpenHelper.COL_TITLE, tittleContent);
//                newRowValues.put(MyDatabaseOpenHelper.COL_DURATION, durationCol);
//                newRowValues.put(MyDatabaseOpenHelper.COL_ALBUMNAME,  albumNameCol);
//                newRowValues.put(MyDatabaseOpenHelper.COL_ALBUMCOVER, albumCoverCol);
//                newRowValues.put(MyDatabaseOpenHelper.COL_LINK, "");
//                newRowValues.put(MyDatabaseOpenHelper.COL_FAVOURITE, false);
//                long newId = db.insert("FAVOUR_SONG", null, newRowValues);
//
//
//            }
//        });

        saveButton.setOnClickListener(click ->{
            ContentValues newRowValues = new ContentValues();

            newRowValues.put(MyDatabaseOpenHelper.COL_TITLE, tittleContent);
            newRowValues.put(MyDatabaseOpenHelper.COL_DURATION, durationCol);
            newRowValues.put(MyDatabaseOpenHelper.COL_ALBUMNAME,  albumNameCol);
            newRowValues.put(MyDatabaseOpenHelper.COL_ALBUMCOVER, albumCoverCol);
            newRowValues.put(MyDatabaseOpenHelper.COL_LINK, "");
            newRowValues.put(MyDatabaseOpenHelper.COL_FAVOURITE, false);

            MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(getActivity());
            db = dbOpener.getWritableDatabase();
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
            Log.i("id", String.valueOf(newId));

            String message1 = "Saved";
            Snackbar.make(saveButton, message1, Snackbar.LENGTH_LONG).show();
        });
        return result;
    }

}