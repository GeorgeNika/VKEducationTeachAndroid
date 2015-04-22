package com.teachandroid.app.api.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.teachandroid.app.LoaderApplication;
import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacadeService;
import com.teachandroid.app.data.Message;
import com.teachandroid.app.util.Logger;

import java.util.HashMap;

public class RequestForSend {

    public static final int TYPE_OF_RECIPIENT_USER = 10;
    public static final int TYPE_OF_RECIPIENT_CHAT = 12;

    private static final String TAG = RequestForSend.class.getSimpleName();
    private static RequestForSend instance;

    private RequestForSend(){
    }

    public static RequestForSend getInstance(){
        if (instance==null) {
            instance = new RequestForSend();
        }
        return instance;
    }

    public void sendMeUsers(Long id){
        IntentWithParameters myIntent = new IntentWithParameters("users.get");
        myIntent.addParameters("user_ids", ""+id);
        myIntent.addParameters("fields", "photo_50,photo_100,photo_200");
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, ApiFacadeService.BROADCAST_USER);
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME, ApiFacadeService.RETURNED_TYPE_USER);
        LoaderApplication.getContext().startService(myIntent.getIntent());
    }

    public void sendMeDialogs() {
        IntentWithParameters myIntent = new IntentWithParameters( "messages.getDialogs");
        myIntent.addParameters("count", "20");
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, ApiFacadeService.BROADCAST_DIALOG);
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME,  ApiFacadeService.RETURNED_TYPE_DIALOG);
        LoaderApplication.getContext().startService(myIntent.getIntent());
    }

    public void sendMeSearchMessage(String query) {
        IntentWithParameters myIntent = new IntentWithParameters( "messages.search");
        myIntent.addParameters("q", query);
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE,ApiFacadeService.BROADCAST_MESSAGE_SEARCH);
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME,  ApiFacadeService.RETURNED_TYPE_MESSAGE);
        LoaderApplication.getContext().startService(myIntent.getIntent());
    }

    public void sendMeMessageHistory(Message message) {

        IntentWithParameters myIntent = new IntentWithParameters( "messages.getHistory");
        if (message.getChatId() == 0) {
            myIntent.addParameters("user_id", message.getUserIdString());
        } else {
            myIntent.addParameters("chat_id", "" + message.getChatId());
        }
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, ApiFacadeService.BROADCAST_MESSAGE);
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME,  ApiFacadeService.RETURNED_TYPE_MESSAGE);
        LoaderApplication.getContext().startService(myIntent.getIntent());
    }

    public void sendMeUsersFromMultiChat(Message message){
        IntentWithParameters myIntent = new IntentWithParameters( "messages.getChatUsers");
        myIntent.addParameters("chat_id", ""+message.getChatId());
        myIntent.addParameters("fields", "photo_50,photo_100,photo_200");
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE,ApiFacadeService.BROADCAST_CHAT_USERS);
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME,  ApiFacadeService.RETURNED_TYPE_USER);
        LoaderApplication.getContext().startService(myIntent.getIntent());

    }

    public void sendMessageWithoutReturnData(Long id, String textMessage, String attachment, int typeOfRecipient){
        IntentWithParameters myIntent = new IntentWithParameters( "messages.send");
        if (typeOfRecipient == TYPE_OF_RECIPIENT_USER) {
            myIntent.addParameters("user_id", "" + id);
        } else if (typeOfRecipient == TYPE_OF_RECIPIENT_CHAT){
            myIntent.addParameters("chat_id", "" + id);
        } else {
            return;
        }
        myIntent.addParameters("message", textMessage);
        myIntent.addParameters("attachment", attachment);
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, ApiFacadeService.BROADCAST_NO_RETURN);
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME,  ApiFacadeService.RETURNED_TYPE_NO_RETURN);
        LoaderApplication.getContext().startService(myIntent.getIntent());
    }

    public void sendMePosts(Long  userId) {
        IntentWithParameters myIntent = new IntentWithParameters( "wall.get");
        myIntent.addParameters("owner_id", "" + userId);
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, ApiFacadeService.BROADCAST_POST);
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME,  ApiFacadeService.RETURNED_TYPE_POST);
        LoaderApplication.getContext().startService(myIntent.getIntent());
    }

    public static void sendNotification(String title, String message){
        Notification.Builder builder =
                new Notification.Builder(LoaderApplication.getContext())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true);


        builder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) LoaderApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }

    public void sendMeFriendsForFirstFillKnownUsers() {
        IntentWithParameters myIntent = new IntentWithParameters( "friends.get");
        myIntent.addParameters("fields", "photo_50,photo_100,photo_200");
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, ApiFacadeService.BROADCAST_NO_RETURN);
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME,  ApiFacadeService.RETURNED_TYPE_FRIENDS_FOR_FIRST_FILL_DATA);
        LoaderApplication.getContext().startService(myIntent.getIntent());
    }

    public void sendMeServerForUploadPhoto() {
        IntentWithParameters myIntent = new IntentWithParameters( "photos.getMessagesUploadServer");
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, ApiFacadeService.BROADCAST_SERVER_FOR_UPLOAD);
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME,  ApiFacadeService.RETURNED_TYPE_SERVER_FOR_UPLOAD);
        LoaderApplication.getContext().startService(myIntent.getIntent());
    }

    public void sendMeSavedPhoto(String server, String photo, String hash){
        IntentWithParameters myIntent = new IntentWithParameters( "photos.saveMessagesPhoto");
        myIntent.addParameters("server", server);
        myIntent.addParameters("photo", photo);
        myIntent.addParameters("hash", hash);
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE,ApiFacadeService.BROADCAST_SAVED_MESSAGE_PHOTO);
        myIntent.addIntentParameters(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME,  ApiFacadeService.RETURNED_TYPE_SAVED_MESSAGE_PHOTO);
        LoaderApplication.getContext().startService(myIntent.getIntent());
    }

    private static class IntentWithParameters {
        private Intent intent;
        private String apiMethod;
        private HashMap<String,String> parameters;

        IntentWithParameters(String apiMethod){
            intent = new Intent(LoaderApplication.getContext(), ApiFacadeService.class);
            parameters = new HashMap<String, String>();
            intent.putExtra(ApiFacadeService.EXTRA_MAIN_COMMAND,apiMethod);
            this.apiMethod = apiMethod;
        }

        public void addParameters(String key, String value){
            parameters.put(key,value);
        }

        public void addIntentParameters(String key, String value){
            intent.putExtra(key,value);
        }

        public Intent getIntent(){
            intent.putExtra(ApiFacadeService.EXTRA_PARAMETERS,parameters);
            Logger.log(TAG, " send request " + apiMethod+ " with parameters "+parameters.values().toString());
            return intent;
        }
    }
}
