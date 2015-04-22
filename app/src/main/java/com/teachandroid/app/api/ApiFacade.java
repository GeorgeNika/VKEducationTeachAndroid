package com.teachandroid.app.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teachandroid.app.api.reponse.ApiResponse;
import com.teachandroid.app.api.reponse.Error;
import com.teachandroid.app.api.reponse.ResponseList;
import com.teachandroid.app.data.Audio;
import com.teachandroid.app.data.AudioAlbum;
import com.teachandroid.app.data.Comment;
import com.teachandroid.app.data.Friend;
import com.teachandroid.app.data.Group;
import com.teachandroid.app.data.Photo;
import com.teachandroid.app.data.Session;
import com.teachandroid.app.data.Video;
import com.teachandroid.app.store.SessionStore;
import com.teachandroid.app.util.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


//TODO: http://vk.com/dev/api_requests

public class ApiFacade {

    private static final String TAG = ApiFacade.class.getSimpleName();
    private final static int DEFAULT_MAX_COUNT = 100;
    private final String accessToken;
    private final String userId;
    private final HttpClient httpClient;
    private final Executor requestExecutor = Executors.newFixedThreadPool(3);

    public ApiFacade(Context context) {
        Session session = SessionStore.restore(context);
        this.accessToken = session.getAccessToken();
        this.userId = session.getUserId();
        this.httpClient = HttpClientFactory.getThreadSafeClient();
    }


    public void getVideo(ResponseListener<List<Video>> listener) {
        RequestBuilder builder = new VkRequestBuilder("video.get", accessToken);
        builder.addParam("count", String.valueOf(DEFAULT_MAX_COUNT));

        Type type = new TypeToken<ApiResponse<ResponseList<Video>>>() {
        }.getType();

        executeGetRequest(builder, type, listener);
    }

    public void searchVideo(String keyWord, ResponseListener<List<Video>> listener) {
        RequestBuilder builder = new VkRequestBuilder("video.search", accessToken);
        builder.addParam("count", String.valueOf(DEFAULT_MAX_COUNT));
        builder.addParam("q", keyWord);

        Type type = new TypeToken<ApiResponse<ResponseList<Video>>>() {
        }.getType();

        executeGetRequest(builder, type, listener);

    }

    public void getAudio(ResponseListener<List<Audio>> listener) {
        RequestBuilder builder = new VkRequestBuilder("audio.get", accessToken);
        builder.addParam("count", String.valueOf(DEFAULT_MAX_COUNT));

        Type type = new TypeToken<ApiResponse<ResponseList<Audio>>>() {
        }.getType();

        executeGetRequest(builder, type, listener);
    }

    public void getAudio(long albumId, ResponseListener<List<Audio>> listener) {
        RequestBuilder builder = new VkRequestBuilder("audio.get", accessToken);
        builder.addParam("count", String.valueOf(DEFAULT_MAX_COUNT));
        builder.addParam("album_id", String.valueOf(albumId));

        Type type = new TypeToken<ApiResponse<ResponseList<Audio>>>() {
        }.getType();

        executeGetRequest(builder, type, listener);
    }

    public void getAudioAlbums(ResponseListener<List<AudioAlbum>> listener) {
        RequestBuilder builder = new VkRequestBuilder("audio.getAlbums", accessToken);
        builder.addParam("count", String.valueOf(DEFAULT_MAX_COUNT));

        Type type = new TypeToken<ApiResponse<ResponseList<AudioAlbum>>>() {
        }.getType();

        executeGetRequest(builder, type, listener);
    }

    public void getPhotos(long albumId, ResponseListener<List<Photo>> listener) {

        RequestBuilder builder = new VkRequestBuilder("photos.get", accessToken);
        builder.addParam("album_id", String.valueOf(albumId));
        builder.addParam("count", String.valueOf(DEFAULT_MAX_COUNT));

        Type type = new TypeToken<ApiResponse<ResponseList<Photo>>>() {
        }.getType();

        executeGetRequest(builder, type, listener);
    }

    public void getGroups(ResponseListener<List<Group>> listener) {
        RequestBuilder builder = new VkRequestBuilder("groups.get", accessToken);
        builder.addParam("count", String.valueOf(DEFAULT_MAX_COUNT));
        builder.addParam("extended", "1");

        Type type = new TypeToken<ApiResponse<ResponseList<Group>>>() {
        }.getType();

        executeGetRequest(builder, type, listener);
    }

    public void getFriends(final ResponseListener<List<Friend>> listener) {
        RequestBuilder builder = new VkRequestBuilder("friends.get", accessToken);
        builder.addParam("count", String.valueOf(DEFAULT_MAX_COUNT));
        builder.addParam("fields", "nickname,photo_200_orig,photo_100");

        Type type = new TypeToken<ApiResponse<ResponseList<Friend>>>() {
        }.getType();

        executeGetRequest(builder, type, listener);
    }

    public void searchAudio(String audioKeyWord, ResponseListener<List<Audio>> listener) {
        RequestBuilder builder = new VkRequestBuilder("audio.search", accessToken);
        builder.addParam("q", audioKeyWord);
        builder.addParam("count", String.valueOf(DEFAULT_MAX_COUNT));

        Type type = new TypeToken<ApiResponse<ResponseList<Audio>>>() {
        }.getType();
        executeGetRequest(builder, type, listener);
    }

    public void getPhotoFromProfile(final ResponseListener<List<Photo>> listener) {
        RequestBuilder builder = new VkRequestBuilder("photos.getProfile", accessToken);

        builder.addParam("count", String.valueOf(DEFAULT_MAX_COUNT));

        Type type = new TypeToken<ApiResponse<ResponseList<Photo>>>() {
        }.getType();
        executeGetRequest(builder, type, listener);

    }


    public void getPhotoAll(final ResponseListener<List<Photo>> listener) {
        RequestBuilder builder = new VkRequestBuilder("photos.getAll", accessToken);

        builder.addParam("count", String.valueOf(DEFAULT_MAX_COUNT));
        builder.addParam("extended", "1");

        Type type = new TypeToken<ApiResponse<ResponseList<Photo>>>() {
        }.getType();
        executeGetRequest(builder, type, listener);
    }

    public void getCommentsPhoto(Photo photo, ResponseListener<List<Comment>> listener) {
        RequestBuilder builder = new VkRequestBuilder("photos.getComments", accessToken);

        builder.addParam("count", String.valueOf(DEFAULT_MAX_COUNT));
        builder.addParam("photo_id", String.valueOf(photo.getId()));

        Type type = new TypeToken<ApiResponse<ResponseList<Comment>>>() {
        }.getType();
        executeGetRequest(builder, type, listener);
    }


    private <T> void executeGetRequest(RequestBuilder builder, final Type type, final ResponseListener<List<T>> listener) {
        String query = builder.query();

        Logger.log(TAG, "api request - " + query);

        final HttpGet request = new HttpGet(query);

        requestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpResponse response = httpClient.execute(request);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    ApiResponse<ResponseList<T>> apiResponse = new Gson().fromJson(reader, type);
                    if (apiResponse != null) {
                        listener.onResponse(apiResponse.getResult().getItems());
                        listener.onError(apiResponse.getError());
                    } else {
                        listener.onError(new Error());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
