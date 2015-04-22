package com.teachandroid.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AudioAlbum implements Parcelable {

    public static final Parcelable.Creator<AudioAlbum> CREATOR = new Parcelable.Creator<AudioAlbum>() {
        public AudioAlbum createFromParcel(Parcel source) {
            return new AudioAlbum(source);
        }

        public AudioAlbum[] newArray(int size) {
            return new AudioAlbum[size];
        }
    };
    @SerializedName("id")
    private long id;
    @SerializedName("owner_id")
    private long ownerId;
    @SerializedName("title")
    private String title;

    public AudioAlbum() {
    }

    private AudioAlbum(Parcel in) {
        this.id = in.readLong();
        this.ownerId = in.readLong();
        this.title = in.readString();
    }

    public long getId() {
        return id;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.ownerId);
        dest.writeString(this.title);
    }
}
