package com.teachandroid.app.api.reponse;


import com.google.gson.annotations.SerializedName;

public class Error {

    @SerializedName("error_code")
    private int mErrorCode;

    @SerializedName("error_msg")
    private String mErrorMessage;

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }
}
