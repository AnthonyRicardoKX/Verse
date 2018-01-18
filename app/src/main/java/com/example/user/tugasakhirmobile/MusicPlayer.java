package com.example.user.tugasakhirmobile;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.tugasakhirmobile.Service.MusicService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer extends Fragment {
    TextView songTitle;
    ImageButton favBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_music_player, container, false);

        songTitle = (TextView) rootView.findViewById(R.id.songTitle);
        favBtn = (ImageButton) rootView.findViewById(R.id.favButton);


        // if song has been picked, set the song title
        if (SplashScreen.globalIndex != -1 && SplashScreen.globalSongList.size() > SplashScreen.globalIndex) {
            int index = SplashScreen.globalIndex;
            String song = SplashScreen.globalSongList.get(index).getTitle();
            boolean songExist = fileExist(song);
            songTitle.setText(song);
            if (!songExist) {
                favBtn.setImageResource(R.drawable.unselected_fav);
            } else {
                favBtn.setImageResource(R.drawable.selected_fav);
            }
        }


        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SplashScreen.globalIndex != -1 && SplashScreen.globalSongList.size() > SplashScreen.globalIndex) {
                    // do something
                    int index = SplashScreen.globalIndex;
                    String song = SplashScreen.globalSongList.get(index).getTitle();
                    boolean songExist = fileExist(song);
                    if (songExist) {
                        deleteSong(song);
                        favBtn.setImageResource(R.drawable.unselected_fav);
                    } else {
                        insertSong(song, (int) SplashScreen.globalSongList.get(index).getID());
                        favBtn.setImageResource(R.drawable.selected_fav);
                    }
                }
            }
        });


        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // if song has been picked, set the song title
            if(SplashScreen.globalIndex != -1 && SplashScreen.globalSongList.size() > SplashScreen.globalIndex) {
                int index = SplashScreen.globalIndex;
                String song = SplashScreen.globalSongList.get(index).getTitle();
                boolean songExist = fileExist(song);
                songTitle.setText(song);
                if(!songExist) {
                    favBtn.setImageResource(R.drawable.unselected_fav);
                }
                else {
                    favBtn.setImageResource(R.drawable.selected_fav);
                }
            }
        }
    }

    public void insertSong(String FILENAME,int musicData) {
        try {
            Toast.makeText(getActivity(), FILENAME + " has been saved", Toast.LENGTH_SHORT).show();
            FileOutputStream fos = getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(Integer.toString(musicData).getBytes());
            fos.close();
        }catch(IOException ex) {
            //Do something with the exception

        }
    }

    public void deleteSong(String FILENAME) {
        File dir = getActivity().getFilesDir();
        File file = new File(dir, FILENAME);
        boolean deleted = file.delete();
        Toast.makeText(getActivity(), FILENAME + " has been deleted", Toast.LENGTH_SHORT).show();
    }

    public boolean fileExist(String fname){
        File file = getActivity().getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }
}
