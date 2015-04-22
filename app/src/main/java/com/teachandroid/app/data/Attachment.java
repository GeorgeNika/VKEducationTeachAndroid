package com.teachandroid.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Attachment implements Parcelable {

    @SerializedName("type")
    String type;

    @SerializedName("photo")
    Photo photo;
    //@SerializedName("posted_photo")
    //@SerializedName("video")
    //@SerializedName("audio")
    //@SerializedName("doc")
    //@SerializedName("graffiti")

    //@SerializedName("link")
    //@SerializedName("note")
    //@SerializedName("app")
    //@SerializedName("poll")
    //@SerializedName("page")
    //@SerializedName("album")
    //@SerializedName("photos_list")


    public Attachment() {
    }

    public String getType() {
        return type;
    }

    public Photo getPhoto() {
        return photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeParcelable(photo,flags);

    }

    public Attachment(Parcel parcel) {
        this.type = parcel.readString();
        this.photo = parcel.readParcelable(getClass().getClassLoader());


    }

    public static final Parcelable.Creator<Attachment> CREATOR = new Parcelable.Creator<Attachment>() {

        public Attachment createFromParcel(Parcel in) {
            return new Attachment(in);
        }

        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };


}
