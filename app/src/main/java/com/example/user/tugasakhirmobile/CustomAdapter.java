package com.example.user.tugasakhirmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.tugasakhirmobile.Model.Song;

import java.util.ArrayList;

/**
 * Created by USER on 28/11/2017.
 */

public class CustomAdapter extends BaseAdapter {
    Context c;
    ArrayList<Song> songs;
    LayoutInflater inflater;

    public CustomAdapter(Context c, ArrayList<Song> songs) {
        this.c = c;
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater ==null) {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null) {
            convertView=inflater.inflate(R.layout.custom_list_view,parent,false);

        }
        TextView music_artist = (TextView) convertView.findViewById(R.id.music_artist);
        TextView music_title = (TextView) convertView.findViewById(R.id.music_title);
        String artist = songs.get(position).getArtist();
        String title = songs.get(position).getTitle();
        music_artist.setText(artist);
        music_title.setText(title);

        return convertView;
    }
}
