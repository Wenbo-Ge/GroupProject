package com.example.groupproject;

public class SoccerVideo {
    protected String country;
    protected String title;
    protected String date;
    protected String side1;
    protected String side2;
    protected String embed;
    protected long id;


    public SoccerVideo(String country, String title, String date, String side1, String side2, String embed) {
        this(country, title, date, side1, side2, embed, -1);
    }

    public SoccerVideo(String country, String title, String date, String side1, String side2, String embed, long id) {
        this.country = country;
        this.title = title;
        this.date = date;
        this.side1 = side1;
        this.side2 = side2;
        this.embed = embed;
        this.id = id;
    }



    public String getCountry () {
        return country;
    }

    public String getTitle () { return title; }

    public String getDate () { return date; }

    public String getSide1 () {
        return side1;
    }

    public String getSide2 () {
        return side2;
    }

    public String getEmbed () {
        return embed;
    }


    public long getId () { return id; }
}
