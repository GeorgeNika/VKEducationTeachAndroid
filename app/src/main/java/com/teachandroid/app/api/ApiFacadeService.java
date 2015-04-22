package com.teachandroid.app.api;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teachandroid.app.LoaderApplication;
import com.teachandroid.app.api.reponse.*;
import com.teachandroid.app.data.Dialog;
import com.teachandroid.app.data.Friend;
import com.teachandroid.app.data.KnownUsers;
import com.teachandroid.app.data.Message;
import com.teachandroid.app.data.Post;
import com.teachandroid.app.data.SavedPhoto;
import com.teachandroid.app.data.Session;
import com.teachandroid.app.data.UpLoadData;
import com.teachandroid.app.data.User;
import com.teachandroid.app.store.SessionStore;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ApiFacadeService extends Service {

    public static final String EXTRA_MAIN_COMMAND = "EXTRA_MAIN_COMMAND";
    public static final String EXTRA_PARAMETERS = "EXTRA_PARAMETERS";
    public static final String EXTRA_RETURNED_BROADCAST_MESSAGE = "EXTRA_RETURNED_BROADCAST_MESSAGE";
    public static final String EXTRA_RETURNED_CLASS_NAME = "EXTRA_RETURNED_CLASS_NAME";

    public static final String RETURNED_TYPE_DIALOG = "DIALOG";
    public static final String RETURNED_TYPE_USER = "USER";
    public static final String RETURNED_TYPE_MESSAGE = "MESSAGE";
    public static final String RETURNED_TYPE_POST = "POST";
    public static final String RETURNED_TYPE_FRIENDS = "FRIENDS";
    public static final String RETURNED_TYPE_FRIENDS_FOR_FIRST_FILL_DATA = "FRIENDS_FOR_FIRST_FILL_DATA";
    public static final String RETURNED_TYPE_SERVER_FOR_UPLOAD = "SERVER_FOR_UPLOAD";
    public static final String RETURNED_TYPE_SAVED_MESSAGE_PHOTO = "SAVED_MESSAGE_PHOTO";
    public static final String RETURNED_TYPE_NO_RETURN = "NO_RETURN";

    public static final String BROADCAST_MESSAGE = "BROADCAST_MESSAGE";
    public static final String BROADCAST_CHAT_USERS = "BROADCAST_CHAT_USERS";
    public static final String BROADCAST_MESSAGE_SEARCH = "BROADCAST_MESSAGE_SEARCH";
    public static final String BROADCAST_DIALOG = "BROADCAST_DIALOG";
    public static final String BROADCAST_USER = "BROADCAST_USER";
    public static final String BROADCAST_FRIEND = "BROADCAST_FRIEND";
    public static final String BROADCAST_POST= "BROADCAST_POST";
    public static final String BROADCAST_SERVER_FOR_UPLOAD= "BROADCAST_SERVER_FOR_UPLOAD"; //???
    public static final String BROADCAST_SAVED_MESSAGE_PHOTO= "BROADCAST_SAVED_MESSAGE_PHOTO";
    public static final String BROADCAST_NO_RETURN = "BROADCAST_NO_RETURN";

    private static final String TAG = ApiFacadeService.class.getSimpleName();

    private final String accessToken;
    private final String userId;
    private final HttpClient httpClient;
    private final Executor requestExecutor = Executors.newFixedThreadPool(3);

    private String mainCommandForRequest ;
    private Map<String,String> parametersForRequest;
    private String returnedBroadcastMessage;
    private String typeOfReturnedData;

    private final Map<String, ProceedData> commandMap;

    public ApiFacadeService() {
        Session session = SessionStore.restore(LoaderApplication.getContext());
        this.accessToken = session.getAccessToken();
        this.userId = session.getUserId();
        this.httpClient = HttpClientFactory.getThreadSafeClient();
        parametersForRequest = new HashMap<String, String>();

        commandMap = new HashMap<String, ProceedData>();
        commandMap.put(RETURNED_TYPE_DIALOG, new ProceedDialogs());
        commandMap.put(RETURNED_TYPE_USER, new ProceedUsers());
        commandMap.put(RETURNED_TYPE_MESSAGE, new ProceedMessages());
        commandMap.put(RETURNED_TYPE_POST, new ProceedPosts());
        commandMap.put(RETURNED_TYPE_FRIENDS, new ProceedFriends());
        commandMap.put(RETURNED_TYPE_FRIENDS_FOR_FIRST_FILL_DATA, new ProceedFriendsForFirstFillData());
        commandMap.put(RETURNED_TYPE_SAVED_MESSAGE_PHOTO, new ProceedSaveMessagePhoto());
        commandMap.put(RETURNED_TYPE_SERVER_FOR_UPLOAD, new ProceedServerForUpload());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        if (intent != null){
            obtainDataFromIntent(intent);
            final HttpGet request = makeRequest();
            requestExecutor.execute(new Runnable() {

                String threadReturnBroadcastMessage  = returnedBroadcastMessage;
                String threadTypeOfReturnedData = typeOfReturnedData;
                @Override
                public void run() {
                    try {
                        HttpResponse response =  httpClient.execute(request);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        StringBuilder stringResponse = new StringBuilder();
                        String tempString;
                        while ((tempString=reader.readLine())!=null){
                            stringResponse.append(tempString);
                        }
                        sendResultData(stringResponse.toString(),threadReturnBroadcastMessage, threadTypeOfReturnedData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        stopSelf(startId);
                    }
                }
            });
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void obtainDataFromIntent(Intent intent){
        mainCommandForRequest=intent.getStringExtra(EXTRA_MAIN_COMMAND);
        parametersForRequest = (Map<String, String>) intent.getSerializableExtra(EXTRA_PARAMETERS);
        returnedBroadcastMessage = intent.getStringExtra(EXTRA_RETURNED_BROADCAST_MESSAGE);
        typeOfReturnedData = intent.getStringExtra(EXTRA_RETURNED_CLASS_NAME);
    }

    private HttpGet makeRequest(){

        RequestBuilder builder = new VkRequestBuilder(mainCommandForRequest, accessToken);
        for (String tempKey :parametersForRequest.keySet()) {
            builder.addParam(tempKey,parametersForRequest.get(tempKey));
        }
        String query = builder.query();
        return new HttpGet(query);
    }

    private void sendResultData(String request, String localReturnedBroadcastMessage, String localTypeOfReturnedData){
        Boolean haveResult= false;
        Intent intent = new Intent(localReturnedBroadcastMessage);
        ProceedData  proceedData = commandMap.get(localTypeOfReturnedData);
        if (proceedData!=null){
            intent.putParcelableArrayListExtra(localReturnedBroadcastMessage,proceedData.getDataFromJson(request));
            haveResult = true;
        }
        if (haveResult){  sendBroadcast(intent); }
    }
}



interface ProceedData<T> {
    ArrayList<T> getDataFromJson(String response);
}

class ProceedDialogs implements ProceedData<Dialog>{
    @Override
    public ArrayList<Dialog> getDataFromJson(String response) {
        ApiResponse<ResponseList<Dialog>> apiResponse = new Gson().fromJson(response, new TypeToken<ApiResponse<ResponseList<Dialog>>>() {}.getType());
        if (apiResponse == null || apiResponse.getResult()==null) {return new ArrayList<Dialog>();}
        return (ArrayList<Dialog>) apiResponse.getResult().getItems();
    }
}
class ProceedMessages implements ProceedData<Message>{
    @Override
    public ArrayList<Message> getDataFromJson(String response) {
        ApiResponse<ResponseList<Message>> apiResponse = new Gson().fromJson(response, new TypeToken<ApiResponse<ResponseList<Message>>>() { }.getType());
        if (apiResponse == null || apiResponse.getResult()==null) {return new ArrayList<Message>();}
        return (ArrayList<Message>) apiResponse.getResult().getItems();
    }
}
class ProceedPosts implements ProceedData<Post>{

    @Override
    public ArrayList<Post> getDataFromJson(String response) {
        ApiResponse<ResponseList<Post>> apiResponse = new Gson().fromJson(response, new TypeToken<ApiResponse<ResponseList<Post>>>() { }.getType());
        if (apiResponse == null || apiResponse.getResult()==null) {return new ArrayList<Post>();}
        return  (ArrayList<Post>) apiResponse.getResult().getItems();
    }
}

class ProceedFriends implements ProceedData<Friend>{
    @Override
    public ArrayList<Friend> getDataFromJson(String response) {
        ApiResponse<ResponseList<Friend>> apiResponse = new Gson().fromJson(response, new TypeToken<ApiResponse<ResponseList<Friend>>>() { }.getType());
        if (apiResponse == null || apiResponse.getResult()==null) {return new ArrayList<Friend>();}
        return  (ArrayList<Friend>) apiResponse.getResult().getItems();
    }
}


class ProceedFriendsForFirstFillData implements ProceedData<Friend>{
    @Override
    public ArrayList<Friend> getDataFromJson(String response) {
        ApiResponse<ResponseList<Friend>> apiResponse = new Gson().fromJson(response, new TypeToken<ApiResponse<ResponseList<Friend>>>() { }.getType());
        if (apiResponse != null && apiResponse.getResult()!=null) {
            for (Friend friend :apiResponse.getResult().getItems()) {
                User tempUser = new User(friend.getUserId());
                tempUser.setFirstName(friend.getFirstName());
                tempUser.setLastName(friend.getLastName());
                //tempUser.setPhoto50(friend.getPhoto50());
                tempUser.setPhoto100(friend.getPhoto100());
                //tempUser.setPhoto200(friend.getPhoto200());
                KnownUsers.getInstance().addUser(friend.getUserId(), tempUser);
            }
        }
        return new ArrayList<Friend>();
    }
}

class ProceedUsers implements ProceedData<User> {
    @Override
    public ArrayList<User> getDataFromJson(String response) {
        int i=0;
        ApiResponse<ArrayList<User>> apiResponse = new Gson().fromJson(response,new TypeToken<ApiResponse<ArrayList<User>>>(){}.getType());
        if(apiResponse != null && apiResponse.getResult()!=null){
            for (User user :apiResponse.getResult()) {
                User tempUser = new User(user.getId());
                tempUser.setFirstName(user.getFirstName());
                tempUser.setLastName(user.getLastName());
                tempUser.setPhoto50(user.getPhoto50());
                tempUser.setPhoto100(user.getPhoto100());
                tempUser.setPhoto200(user.getPhoto200());
                KnownUsers.getInstance().addUser(user.getId(), tempUser);
            }
            ArrayList<User> result = apiResponse.getResult();
        }
        return new ArrayList<User>();
    }
}

class ProceedSaveMessagePhoto implements ProceedData<SavedPhoto> {

    @Override
    public ArrayList<SavedPhoto> getDataFromJson(String response) {

        ApiResponse<ArrayList<SavedPhoto>> apiResponse = new Gson().fromJson(response, new TypeToken<ApiResponse<ArrayList<SavedPhoto>>>(){}.getType());
        //ArrayList<SavedPhoto> resultArray = new ArrayList<SavedPhoto>();
        if (apiResponse == null || apiResponse.getResult()==null) {new ArrayList<SavedPhoto>();}
        //SavedPhoto savedPhoto= apiResponse.getResult();
        //resultArray.add(apiResponse.getResult());
        return  apiResponse.getResult();
    }
}

class ProceedServerForUpload implements ProceedData<UpLoadData>{
    @Override
    public ArrayList<UpLoadData> getDataFromJson(String response) {
        ApiResponse<UpLoadData> apiResponse = new Gson().fromJson(response, new TypeToken<ApiResponse<UpLoadData>>(){}.getType());
        ArrayList<UpLoadData> resultArray = new ArrayList<UpLoadData>();
        if (apiResponse == null || apiResponse.getResult()==null) {return resultArray;}
        resultArray.add(apiResponse.getResult());
        return  resultArray;
    }
}

