package com.example.groupproject;

public class ActivitySongDetails {
//  title, duration, album name and album cover
    protected  String title;
    protected  int duration;
    protected  String albumName;
    protected boolean albumCover;
    public ActivitySongDetails(String tittle, int duration, String albumName, boolean albumCover){
        this.title=tittle;
        this.duration=duration;
        this.albumName=albumName;
        this.albumCover=albumCover;
    }
    public String getTitle(){
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public String getAlbumName() {
        return albumName;
    }

    public boolean isAlbumCover() {
        return albumCover;
    }
}
