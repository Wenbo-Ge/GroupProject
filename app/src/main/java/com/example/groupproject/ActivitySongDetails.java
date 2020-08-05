package com.example.groupproject;

import java.util.ArrayList;
import java.util.List;

public class ActivitySongDetails {

    private long id;
    private String title;
    private String duration;
    private String albumName;
    private String artist;
    private String link;
    private String albumCover;
    private boolean isFavourite = false;
    public ActivitySongDetails() {
    }

    public ActivitySongDetails(long id, String title, String duration, String albumName, String artist, String link,String albumCover,
                boolean isFavourite) {
        this.id=id;
        this.title=title;
        this.duration=duration;
        this.albumName=albumName;
        this.artist=artist;
        this.link=link;
        this.albumCover = albumCover;
        this.isFavourite=isFavourite;
    }
//    public ActivitySongDetails(String title) {
//        setTitle(title);
//    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getAlbumName() { return albumName; }
    public void setAlbumName(String albumName) { this.albumName = albumName; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public boolean getIsFavourite() { return isFavourite; }
    public void setIsFavourite(boolean isFavourite) { this.isFavourite = isFavourite; }
    public String getAlbumCover() { return albumCover; }
    public void setAlbumCover(String albumCover) { this.albumCover = albumCover; }
    public static List<ActivitySongDetails> getAllSongs() {
        List<ActivitySongDetails> songs = new ArrayList<ActivitySongDetails>();
        return songs;
    }

    @Override
    public String toString() {
        super.toString();
        String songInformation = " "+title + "\n "+artist+"   Duration=" + duration + "\n AlbumName=" + albumName
                + "\n Link=" + link ;
        return songInformation;
    }


}

