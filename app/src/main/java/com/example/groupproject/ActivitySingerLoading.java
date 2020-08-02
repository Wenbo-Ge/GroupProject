package com.example.groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySingerLoading extends AppCompatActivity {

    ProgressBar mProgressbar;
    TextView mLoadingText;
    int mProgressStatus= 0;
    Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer_loading);

        Intent goToSongsList = new Intent(ActivitySingerLoading.this,ActivitySongList.class);

        mProgressbar = findViewById(R.id.progressBar);
        mLoadingText=findViewById(R.id.loadingTextview);
        mProgressStatus = mProgressbar.getProgress();

        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus++;
                    android.os.SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgressbar.setProgress(mProgressStatus);
                            if (mProgressStatus == 100)
                                startActivity(goToSongsList);
                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingText.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();

    }
}
