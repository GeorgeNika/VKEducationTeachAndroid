package com.teachandroid.app.data;

import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("id")
    private long id ;

    @SerializedName("owner_id")
    private long owner_id ;

    @SerializedName("title")
    private String title ;

    @SerializedName("duration")
    private long duration ;

    @SerializedName("description")
    private String description ;

    @SerializedName("date")
    private long date ;

    @SerializedName("views")
    private long views;

    @SerializedName("comments")
    private long comments;

    @SerializedName("photo_130")
    private String photo_130;

    @SerializedName("photo_320")
    private String photo_320;

    @SerializedName("photo_640")
    private String photo_640;

    @SerializedName("adding_date")
    private long adding_date;

    @SerializedName("player")
    private String player;

    public long getId() {
        return id;
    }

    public long getOwner_id() {
        return owner_id;
    }

    public String getTitle() {
        return title;
    }

    public long getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public long getDate() {
        return date;
    }

    public long getComments() {
        return comments;
    }

    public long getViews() {
        return views;
    }

    public String getPhoto_130() {
        return photo_130;
    }

    public String getPhoto_320() {
        return photo_320;
    }

    public String getPhoto_640() {
        return photo_640;
    }

    public long getAdding_date() {
        return adding_date;
    }

    public String getPlayer() {
        return player;
    }
}
