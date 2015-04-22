package com.teachandroid.app.api.reponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseList<T> {

    @SerializedName("count")
    private int count;

    @SerializedName("items")
    private List<T> items;

    public int getCount() {
        return count;
    }

    public List<T> getItems() {
        return items;
    }
}
