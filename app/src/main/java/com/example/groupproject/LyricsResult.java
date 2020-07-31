package com.example.groupproject;

public class LyricsResult {
    private String artist;
    private String title;
    private String lyrics;
    private long id;

    public LyricsResult(String artist, String title, String lyrics, long id) {
        this.artist = artist;
        this.title = title;
        this.lyrics = lyrics;
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getLyrics() {
        return lyrics;
    }

    public long getId() {
        return id;
    }
}
