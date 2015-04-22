package com.teachandroid.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
/**
 * Created by rakovskyi on 26.02.15.
 *gid 	Community ID.
 positive number
 name 	Community name.
 string
 photo 	URL of the 50px-wide community logo.
 string
 */
public class Group implements Parcelable{
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(gid);
        dest.writeString(name);
        dest.writeString(photo);

    }

    public Group() {
    }

    public Group(int gid, String name, String photo) {

        this.gid = gid;
        this.name = name;
        this.photo = photo;
    }

    public Group(Parcel in){
        this.gid=in.readInt();
        this.name=in.readString();
        this.photo=in.readString();

    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {

        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @SerializedName("id")
     private  int gid;
    @SerializedName("name")
    private  String name;
    @SerializedName("photo_50")
    private String photo;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
