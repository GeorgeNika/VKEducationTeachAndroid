package com.teachandroid.app.api;

public interface RequestBuilder {

    String query();

    void addParam(String key, String value);
}
