package com.example.user.tugasakhirmobile;

/**
 * Created by USER on 28/11/2017.
 */

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.user.tugasakhirmobile.Model.Song;
import com.example.user.tugasakhirmobile.Service.MusicService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Favourite extends Fragment implements MediaController.MediaPlayerControl {
    private ArrayList<Song> songList;
    private ListView songView;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    private MusicController controller;
    private boolean paused=false, playbackPaused=false;
    CustomAdapter adapter;
    ListView listview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favourite, container, false);
        listview=(ListView)rootView.findViewById(R.id.listView);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SplashScreen.globalIndex = position;
                SplashScreen.globalSongList = songList;
                musicSrv.setList(songList);
                songPicked(position);
                ((TabMenu)getActivity()).navigateFragment(2);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

            }

        }

        songList = new ArrayList<Song>();
        getSongList();
        Collections.sort(songList, new Comparator<Song>(){
            public int compare(Song a, Song b)
            {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        adapter = new CustomAdapter(this.getActivity(),songList);
        listview.setAdapter(adapter);
        setController();
        controller.show(0);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getSongList();
            Collections.sort(songList, new Comparator<Song>(){
                public int compare(Song a, Song b)
                {
                    return a.getTitle().compareTo(b.getTitle());
                }
            });
            adapter.notifyDataSetChanged();
        }
    }
    
    public void getSongList() {
        songList.clear();
        for(int i=0;i<getActivity().fileList().length;i++) {


            int ch;
            StringBuffer fileContent = new StringBuffer("");
            FileInputStream fis;
            try {
                fis = getActivity().openFileInput( getActivity().fileList()[i] );
                try {
                    while( (ch = fis.read()) != -1)
                        fileContent.append((char)ch);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String data = new String(fileContent);

            long thisId = Integer.parseInt(data);
            String thisTitle = getActivity().fileList()[i];
            String thisArtist = "";
            songList.add(new Song(thisId, thisTitle, thisArtist));
        }

    }


    @Override
    public void onStart()
    {
        super.onStart();
        if (playIntent == null)
        {
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
    }

    @Override
    public void onDestroy()
    {
        getActivity().stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }

    @Override
    public void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    public void onResume(){
        super.onResume();
        if (paused){
            setController();
            paused=false;
        }
    }

    @Override
    public void onStop() {
        controller.hide();
        super.onStop();
    }

    private ServiceConnection musicConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list

            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public void songPicked(int index)
    {
        musicSrv.setSong(index);
        musicSrv.playSong();

        if(playbackPaused){
            playbackPaused=true;
            setController();
            playbackPaused=false;
        }

        controller.show(0);
    }


    private void setController()
    {
        controller = new MusicController(getActivity());

        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        controller.setAnchorView(getActivity().findViewById(R.id.main_content));
        controller.setEnabled(true);
    }


    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    //play previous
    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicSrv!=null && musicBound && musicSrv.isPng()) return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public int getDuration() {
        if (musicSrv!=null && musicBound && musicSrv.isPng()) return musicSrv.getDur();
        else return 0;
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound) return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public void pause() {
        playbackPaused = true;
        musicSrv.pausePlayer();
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public void start() {
        musicSrv.go();
    }


}
