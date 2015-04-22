package com.teachandroid.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UpLoadData implements Parcelable{

    @SerializedName("upload_url")
    private String uploadUrl;
    @SerializedName("album_id")
    private Long albumId;
    @SerializedName("user_id")
    private Long userId;

    public String getUploadUrl() {
        return uploadUrl;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public Long getUserId() {
        return userId;
    }


    public static final Parcelable.Creator<UpLoadData> CREATOR = new Parcelable.Creator<UpLoadData>() {
        public UpLoadData createFromParcel(Parcel source) {
            return new UpLoadData(source);
        }

        public UpLoadData[] newArray(int size) {
            return new UpLoadData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uploadUrl);
        dest.writeLong(albumId);
        dest.writeLong(userId);
    }

    private UpLoadData(Parcel parcel) {
        this.uploadUrl =parcel.readString();
        this.albumId =parcel.readLong();
        this.userId =parcel.readLong();
    }
}
