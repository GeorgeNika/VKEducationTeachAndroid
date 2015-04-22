package com.teachandroid.app.data;


import com.google.gson.annotations.SerializedName;

public class Photos {

    @SerializedName("id")
    private long id ;

    @SerializedName("album_id")
    private long album_id ;

    @SerializedName("owner_id")
    private long owner_id ;

    @SerializedName("photo_75")
    private String photo_75;

    @SerializedName("photo_130")
    private String photo_130;

    @SerializedName("photo_604")
    private String photo_604;

    @SerializedName("photo_807")
    private String photo_807;

    @SerializedName("photo_1280")
    private String photo_1280;

    @SerializedName("photo_2560")
    private String photo_2560;

    @SerializedName("text")
    private String text;

    @SerializedName("date")
    private long date ;

    @SerializedName("post_id")
    private long post_id ;


}
