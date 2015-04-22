package com.teachandroid.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Friend implements Parcelable {

    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>() {
        public Friend createFromParcel(Parcel source) {
            return new Friend(source);
        }

        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };
    @SerializedName("id")
    private long userId;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("photo_200_orig")
    private String photo200Orig;
    @SerializedName("photo_100")
    private String photo100;
    @SerializedName("online")
    private int online;

    public Friend() {
    }

    private Friend(Parcel in) {
        this.userId = in.readLong();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.nickname = in.readString();
        this.photo200Orig = in.readString();
        this.photo100 = in.readString();
        this.online = in.readInt();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", photo200Orig='" + photo200Orig + '\'' +
                ", photo100='" + photo100 + '\'' +
                ", online=" + online +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userId);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.nickname);
        dest.writeString(this.photo200Orig);
        dest.writeString(this.photo100);
        dest.writeInt(this.online);
    }
}
