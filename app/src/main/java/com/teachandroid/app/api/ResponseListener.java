package com.teachandroid.app.api;

import com.teachandroid.app.api.reponse.Error;
import com.teachandroid.app.data.Video;

import java.util.List;

public interface ResponseListener<T> {

    void onResponse(T response);

    void onError(Error error);
}
