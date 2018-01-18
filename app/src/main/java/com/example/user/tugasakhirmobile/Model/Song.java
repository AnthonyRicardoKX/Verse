package com.example.user.tugasakhirmobile.Model;

/**
 * Created by USER on 06/12/2017.
 */

public class Song {
    private long id;
    private String title;
    private String artist;

    public long getID(){ return id; }
    public String getTitle(){ return title; }
    public String getArtist(){ return artist; }

    public Song(long songID, String songTitle, String songArtist)
    {
        this.id = songID;
        this.title = songTitle;
        this.artist = songArtist;
    }
    public Song(String songTitle, String songArtist)
    {
        this.title = songTitle;
        this.artist = songArtist;
    }
}