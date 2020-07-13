package com.example.groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class SoccerLoading extends AppCompatActivity {
    Intent goToVideo;
    ProgressBar pbar;
    int a = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_loading_page);

        // launch video page
        goToVideo = new Intent(SoccerLoading.this, SoccerVideoActivity.class);
        pbar = findViewById(R.id.p_Bar);



                a = pbar.getProgress();
                new Thread(new Runnable() {
                    public void run() {
                        while (a < 100) {
                            a += 10;
                            handler.post(new Runnable() {
                                public void run() {
                                    pbar.setProgress(a);
                                    if (a == 100)
                                        startActivity(goToVideo);
                                }
                            });
                            try {
                                // Sleep for 50 ms to show progress you can change it as well.
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

    }
}
