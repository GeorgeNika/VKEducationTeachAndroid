package com.teachandroid.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Photo implements Parcelable {

    @SerializedName("id")
    private long id;

    @SerializedName("photo_200_orig")
    private String photo200Orig;

    @SerializedName("photo_100")
    private String photo100;

    @SerializedName("album_id")
    private long albumId;

    @SerializedName("owner_id")
    private long ownerId;

    @SerializedName("photo_75")
    private String photo75;

    @SerializedName("photo_130")
    private String photo130;

    @SerializedName("photo_604")
    private String photo604;

    @SerializedName("photo_807")
    private String photo807;

    @SerializedName("photo_1280")
    private String photo1280;

    @SerializedName("photo_2560")
    private String photo2560;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    @SerializedName("text")
    private String text;

    @SerializedName("date")
    private long date;

    @SerializedName("post_id")
    private long postId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoto200Orig() {
        return photo200Orig;
    }

    public void setPhoto200Orig(String photo200Orig) {
        this.photo200Orig = photo200Orig;
    }

    public String getPhoto100() {
        return photo100;
    }

    public void setPhoto100(String photo100) {
        this.photo100 = photo100;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getPhoto75() {
        return photo75;
    }

    public void setPhoto75(String photo75) {
        this.photo75 = photo75;
    }

    public String getPhoto130() {
        return photo130;
    }

    public void setPhoto130(String photo130) {
        this.photo130 = photo130;
    }

    public String getPhoto604() {
        return photo604;
    }

    public void setPhoto604(String photo604) {
        this.photo604 = photo604;
    }

    public String getPhoto807() {
        return photo807;
    }

    public void setPhoto807(String photo807) {
        this.photo807 = photo807;
    }

    public String getPhoto1280() {
        return photo1280;
    }

    public void setPhoto1280(String photo1280) {
        this.photo1280 = photo1280;
    }

    public String getPhoto2560() {
        return photo2560;
    }

    public void setPhoto2560(String photo2560) {
        this.photo2560 = photo2560;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return id + "OwnerID" + ownerId;
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {

        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(photo200Orig);
        dest.writeString(photo100);
        dest.writeLong(albumId);
        dest.writeLong(ownerId);
        dest.writeString(photo75);
        dest.writeString(photo130);
        dest.writeString(photo604);
        dest.writeString(photo807);
        dest.writeString(photo1280);
        dest.writeString(photo2560);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(text);
        dest.writeLong(date);
        dest.writeLong(postId);
    }

    public Photo(Parcel parcel) {
        this.id =parcel.readLong();
        this.photo200Orig =parcel.readString();
        this.photo100 =parcel.readString();
        this.albumId =parcel.readLong();
        this.ownerId =parcel.readLong();
        this.photo75 =parcel.readString();
        this.photo130 =parcel.readString();
        this.photo604 =parcel.readString();
        this.photo807 =parcel.readString();
        this.photo1280 =parcel.readString();
        this.photo2560 =parcel.readString();
        this.width =parcel.readInt();
        this.height =parcel.readInt();
        this.text =parcel.readString();
        this.date =parcel.readLong();
        this.postId =parcel.readLong();
    }
}