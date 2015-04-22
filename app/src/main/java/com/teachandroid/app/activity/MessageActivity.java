package com.teachandroid.app.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.api.service.RequestForSend;
import com.teachandroid.app.data.KnownUsers;
import com.teachandroid.app.data.Message;
import com.teachandroid.app.data.User;
import com.teachandroid.app.api.ApiFacadeService;
import com.teachandroid.app.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends ActionBarActivity {

    private static final String TAG = MessageActivity.class.getSimpleName();
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    private Intent intent;
    private Message message;
    private User user;

    private MessageAdapter messageAdapter;
    private LinearLayout chatUsersLinearLayout;
    private ListView  messageList;
    private List<ImageView> imageViewList = new ArrayList<ImageView>();

    private MessageReceiver receiverMessage;
    private UserReceiver receiverUser;
    private ChatUsersReceiver receiverChatUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            obtainDataFromIntent();
            initializeVariables();
            fillFieldsOnActivity();
            setAdapters();
            setListeners();
            if (savedInstanceState != null) {
                //todo reaction on rotate;
            }
            registerAllNeededReceiver();
            RequestForSend.getInstance().sendMeMessageHistory(message);
            if (message.getChatId()!=0) {
                RequestForSend.getInstance().sendMeUsersFromMultiChat(message);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unRegisterAllNeededReceiver();
        super.onDestroy();
    }

    private void obtainDataFromIntent(){
        intent = getIntent();
        if (intent==null) {
            throw new NullPointerException("No intent");
        }
    }

    private void initializeVariables(){
        message = intent.getParcelableExtra(MessageActivity.EXTRA_MESSAGE);
        user = KnownUsers.getInstance().getUserFromId(message.getUserId());

    }

    private void fillFieldsOnActivity(){
        setContentView(R.layout.activity_message);

        ImageView imageView = (ImageView)findViewById(R.id.image_message_top);
        TextView textOwnerData = (TextView) findViewById(R.id.text_message_owner_data);
        TextView textTitleData = (TextView) findViewById(R.id.text_message_title_data);
        chatUsersLinearLayout = (LinearLayout)findViewById(R.id.horizontal_linear_scroll);
        messageList = (ListView) findViewById(R.id.list_message);

        ImageLoader.getInstance().displayImage(user.getPhoto100(),imageView);
        textTitleData.setText(message.getTitle());
        textOwnerData.setText(user.getFirstName() + " - " +user.getLastName());
    }

    private void setAdapters(){
        messageAdapter = new MessageAdapter(this, new ArrayList<Message>());
        messageList.setAdapter(messageAdapter);
    }

    private void setListeners(){
        Button buttonSend = (Button)findViewById(R.id.button_send_message);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.edit_send_message);
                if (editText.getText().toString().equals("")) {return;}

                if (message.getChatId() == 0) {
                    RequestForSend.getInstance().sendMessageWithoutReturnData(message.getUserId(),
                            editText.getText().toString(),
                            "",
                            RequestForSend.TYPE_OF_RECIPIENT_USER);
                } else {
                    RequestForSend.getInstance().sendMessageWithoutReturnData(message.getChatId(),
                            editText.getText().toString(),
                            "",
                            RequestForSend.TYPE_OF_RECIPIENT_CHAT);
                }

                editText.setText("");

                RequestForSend.getInstance().sendNotification(
                        getString(R.string.text_send_message_to)
                                + KnownUsers.getInstance().getUserFromId(message.getUserId()).getFirstName()
                                + " "
                                + KnownUsers.getInstance().getUserFromId(message.getUserId()).getLastName(),
                        editText.getText().toString());
            }
        });

        ImageView mainImage = (ImageView)findViewById(R.id.image_message_top);
        mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),UserActivity.class);
                intent.putExtra(UserActivity.EXTRA_USER_ID,user.getId());
                startActivity(intent);
            }
        });


    }





    private static final class MessageAdapter extends ArrayAdapter<Message> {

        public MessageAdapter(Context context, List<Message> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.list_item_message, null);
            }
            TextView textOwnerData = (TextView) convertView.findViewById(R.id.text_message_owner_data);
            TextView messageBodyData = (TextView) convertView.findViewById(R.id.text_message_body_data);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.image_message);

            Message message = getItem(position);
            final User tempUser = KnownUsers.getInstance().getUserFromId(message.getFromId());
            ImageLoader.getInstance().displayImage(tempUser.getPhoto50(),imageView);
            textOwnerData.setText(tempUser.getFirstName() + " - " +tempUser.getLastName());
            messageBodyData.setText("" + message.getBody());

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),UserActivity.class);
                    intent.putExtra(UserActivity.EXTRA_USER_ID, tempUser.getId());
                    v.getContext().startActivity(intent);
                }
            });

            return convertView;
        }
    }

    private void registerAllNeededReceiver() {
        receiverMessage = new MessageReceiver();
        registerReceiver(receiverMessage,new IntentFilter(ApiFacadeService.BROADCAST_MESSAGE));
        receiverUser = new UserReceiver();
        registerReceiver(receiverUser,new IntentFilter(ApiFacadeService.BROADCAST_USER));
        receiverChatUsers = new ChatUsersReceiver();
        registerReceiver(receiverChatUsers,new IntentFilter(ApiFacadeService.BROADCAST_CHAT_USERS));
    }

    private void unRegisterAllNeededReceiver() {
        if (receiverMessage!=null) {
            unregisterReceiver(receiverMessage);
        }
        if (receiverUser!=null) {
            unregisterReceiver(receiverUser);
        }
        if (receiverChatUsers!=null) {
            unregisterReceiver(receiverChatUsers);
        }
    }

    private class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null ) {return;}
            Logger.log(TAG,"obtain messages from broadcast");
            ArrayList<Message> result;
            result = intent.getParcelableArrayListExtra(ApiFacadeService.BROADCAST_MESSAGE);
            messageAdapter.clear();
            messageAdapter.addAll(result);
            messageAdapter.notifyDataSetChanged();
        }
    }
    private class UserReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            messageAdapter.notifyDataSetChanged();
        }
    }

    private class ChatUsersReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null ) {return;}
            Logger.log(TAG,"obtain users from broadcast");
            List<Long> listChatUsers = message.getChatActive();
            for (final Long tempId :listChatUsers){
                ImageView tempImageView = new ImageView(chatUsersLinearLayout.getContext());
                chatUsersLinearLayout.addView(tempImageView);
                tempImageView.setPadding(3, 1, 3, 1);
                imageViewList.add(tempImageView);
                tempImageView.setOnClickListener(new View.OnClickListener() {
                    private Long id = tempId;
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(),UserActivity.class);
                        intent.putExtra(UserActivity.EXTRA_USER_ID,id);
                        startActivity(intent);
                    }
                });
                ImageLoader.getInstance().displayImage(KnownUsers.getInstance().getUserFromId(tempId).getPhoto50(), imageViewList.get(imageViewList.size()-1));
            }
        }
    }
}