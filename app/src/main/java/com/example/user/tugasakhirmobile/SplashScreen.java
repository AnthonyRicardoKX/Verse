package com.example.user.tugasakhirmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.user.tugasakhirmobile.Model.Song;

import java.util.ArrayList;


public class SplashScreen extends AppCompatActivity {
    public static ArrayList<Song> globalSongList;
    public static int globalIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(),TabMenu.class);
                    startActivity(intent);
                    finish();
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
