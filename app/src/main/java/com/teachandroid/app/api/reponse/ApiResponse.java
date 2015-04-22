package com.teachandroid.app.api.reponse;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<TResult> {

    @SerializedName("response")
    private TResult result;

    @SerializedName("error")
    private Error error;

    public TResult getResult() {
        return result;
    }

    public Error getError() {
        return error;
    }

}