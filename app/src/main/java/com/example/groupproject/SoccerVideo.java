package com.example.groupproject;

public class SoccerVideo {
    protected String country;
    protected String date;
    protected String side1;
    protected String side2;
    protected String embed;
    protected boolean liked;
    protected long id;



    public SoccerVideo(String country, String date, String side1, String side2, String embed, boolean liked, long id) {
        this.country = country;
        this.date = date;
        this.side1 = side1;
        this.side2 = side2;
        this.embed = embed;
        this.liked = liked;
        this.id = id;
    }



    public String getCountry () {
        return country;
    }

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

    public boolean getLiked () { return liked; }


    public long getId () { return id; }
}
