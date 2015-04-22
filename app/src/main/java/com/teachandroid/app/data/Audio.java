package com.teachandroid.app.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Audio implements Parcelable {

    public static final Parcelable.Creator<Audio> CREATOR = new Parcelable.Creator<Audio>() {
        public Audio createFromParcel(Parcel source) {
            return new Audio(source);
        }

        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };
    @SerializedName("id")
    private long id;
    @SerializedName("owner_id")
    private long ownerId;
    @SerializedName("artist")
    private String artist;
    @SerializedName("title")
    private String title;
    @SerializedName("duration")
    private int duration;
    @SerializedName("url")
    private String url;
    @SerializedName("lyrics_id")
    private int lyricsId;
    @SerializedName("genre")
    private int genre;

    public Audio() {
    }

    private Audio(Parcel in) {
        this.id = in.readLong();
        this.ownerId = in.readLong();
        this.artist = in.readString();
        this.title = in.readString();
        this.duration = in.readInt();
        this.url = in.readString();
        this.lyricsId = in.readInt();
        this.genre = in.readInt();
    }

    public long getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.ownerId);
        dest.writeString(this.artist);
        dest.writeString(this.title);
        dest.writeInt(this.duration);
        dest.writeString(this.url);
        dest.writeInt(this.lyricsId);
        dest.writeInt(this.genre);
    }
}
