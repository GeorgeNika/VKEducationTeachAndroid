package com.teachandroid.app.api.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import com.teachandroid.app.api.ApiFacade;
import com.teachandroid.app.api.ResponseListener;
import com.teachandroid.app.api.SimpleResponseListener;
import com.teachandroid.app.data.Audio;
import com.teachandroid.app.data.AudioAlbum;
import com.teachandroid.app.data.Friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Igor Kuzmenko on 12.03.2015.
 * Started service. It processes requests using {@link ApiFacade} class and sends  responses using broadcast messages.
 * Incoming intent MUST contain an extra parameter EXTRA_SERVICE_REQUEST which contains one of  REQUEST_XXXXX_XXXXX values.
 * <p/>
 * Response broadcast should be filtered using REQUEST_XXXXX_XXXXX value.
 * Example:
 * broadcastManager.registerReceiver(friendsReceiver, new IntentFilter(ApiService.REQUEST_GET_FRIENDS));
 */

public class ApiService extends Service {

    public static final String REQUEST_GET_FRIENDS = "friends.get";
    public static final String REQUEST_GET_AUDIO = "audio.get";
    public static final String REQUEST_GET_AUDIO_ALBUMS = "audio.getAlbums";

    public static final String EXTRA_SERVICE_RESPONSE = "service_response";
    public static final String EXTRA_SERVICE_REQUEST = "service_request";
    public static final String EXTRA_AUDIO_ALBUM_ID = "audio_album_id";

    private HashMap<String, RequestCommand> commands = new HashMap<String, RequestCommand>();

    private ApiFacade facade;

    public ApiService() {
        initCommands();
    }

    private void initCommands() {
        commands.put(REQUEST_GET_FRIENDS, new GetFriends());
        commands.put(REQUEST_GET_AUDIO, new GetAudio());
        commands.put(REQUEST_GET_AUDIO_ALBUMS, new GetAudioAlbum());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        facade = new ApiFacade(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        for (String item : commands.keySet()) {
            if (item.equals(intent.getStringExtra(EXTRA_SERVICE_REQUEST))) {
                commands.get(item).execute(intent);
            }
        }
        return START_REDELIVER_INTENT;
    }

    private <T extends Parcelable> ResponseListener<List<T>> getResponseListener(final String requestAction, Class<T> dataType) {
        return new SimpleResponseListener<List<T>>() {
            @Override
            public void onResponse(List<T> response) {
                Intent responseIntent = new Intent();
                responseIntent.setAction(requestAction);
                responseIntent.putParcelableArrayListExtra(EXTRA_SERVICE_RESPONSE, (ArrayList<T>) response);
                LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(responseIntent);
            }
        };
    }

    public interface RequestCommand {
        void execute(Intent intent);
    }

    private class GetFriends implements RequestCommand {

        @Override
        public void execute(Intent intent) {
            facade.getFriends(getResponseListener(REQUEST_GET_FRIENDS, Friend.class));
        }
    }

    private class GetAudio implements RequestCommand {

        @Override
        public void execute(Intent intent) {
            if (intent.hasExtra(EXTRA_AUDIO_ALBUM_ID)) {
                facade.getAudio(intent.getLongExtra(EXTRA_AUDIO_ALBUM_ID, -1)
                        , getResponseListener(REQUEST_GET_AUDIO, Audio.class));
                return;
            }
            facade.getAudio(getResponseListener(REQUEST_GET_AUDIO, Audio.class));
        }
    }

    private class GetAudioAlbum implements RequestCommand {

        @Override
        public void execute(Intent intent) {
            facade.getAudioAlbums(getResponseListener(REQUEST_GET_AUDIO_ALBUMS, AudioAlbum.class));
        }
    }
}
