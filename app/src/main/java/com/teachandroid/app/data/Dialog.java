package com.teachandroid.app.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Dialog implements Parcelable{

    @SerializedName("unread")
    private int unread;

    @SerializedName("message")
    private Message message;

    public int getUnread() {
        return unread;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Dialog> CREATOR = new Parcelable.Creator<Dialog>() {

        public Dialog createFromParcel(Parcel in) {
            return new Dialog(in);
        }

        public Dialog[] newArray(int size) {
            return new Dialog[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(unread);
        dest.writeParcelable(message,flags);
    }

    public Dialog(Parcel parcel) {
        this.unread=parcel.readInt();
        this.message=parcel.readParcelable(getClass().getClassLoader());
    }

    public Dialog(int unread, Message message) {
        this.unread = unread;
        this.message = message;
    }
}
