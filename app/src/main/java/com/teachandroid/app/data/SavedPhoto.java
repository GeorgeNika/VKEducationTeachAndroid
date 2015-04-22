package com.teachandroid.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SavedPhoto implements Parcelable {

    @SerializedName("id")
    private Long id;
    @SerializedName("owner_id")
    private Long ownerId;

    public static final Parcelable.Creator<SavedPhoto> CREATOR = new Parcelable.Creator<SavedPhoto>() {
        public SavedPhoto createFromParcel(Parcel source) {
            return new SavedPhoto(source);
        }

        public SavedPhoto[] newArray(int size) {
            return new SavedPhoto[size];
        }
    };

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(ownerId);
    }

    private SavedPhoto(Parcel parcel) {
        this.id =parcel.readLong();
        this.ownerId =parcel.readLong();
    }
}
