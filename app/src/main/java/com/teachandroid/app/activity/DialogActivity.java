package com.teachandroid.app.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.api.service.RequestForSend;
import com.teachandroid.app.data.Dialog;
import com.teachandroid.app.data.KnownUsers;
import com.teachandroid.app.data.Message;
import com.teachandroid.app.api.ApiFacadeService;
import com.teachandroid.app.data.User;
import com.teachandroid.app.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DialogActivity extends Activity {

    private static final String TAG = DialogActivity.class.getSimpleName();

    private DialogAdapter dialogAdapter;
    private ListView dialogList;
    private SearchView actionView;

    private DialogReceiver receiverDialog;
    private DialogSearchReceiver receiverDialogSearch;
    private UserReceiver receiverUser;


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
            RequestForSend.getInstance().sendMeFriendsForFirstFillKnownUsers(); //must run on start application
            RequestForSend.getInstance().sendMeDialogs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dialog_menu, menu);
        MenuItem item = menu.findItem(R.id.action_dialog_search);
        actionView = (SearchView) item.getActionView();
        actionView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RequestForSend.getInstance().sendMeSearchMessage(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterAllNeededReceiver();
    }

    private void obtainDataFromIntent(){   // reserve for future
    }

    private void initializeVariables(){    // reserve for future
    }

    private void fillFieldsOnActivity(){
        setContentView(R.layout.activity_dialog);
        dialogList = (ListView) findViewById(R.id.list_dialog);
    }

    private void setAdapters(){
        dialogAdapter = new DialogAdapter(this, new ArrayList<Dialog>());
        dialogList.setAdapter(dialogAdapter);
    }

    private void setListeners(){
        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DialogActivity.this,MessageActivity.class);
                Message message = ((Dialog)parent.getAdapter().getItem(position)).getMessage();
                intent.putExtra(MessageActivity.EXTRA_MESSAGE,message);
                startActivity(intent);
            }
        });
    }


    public static final class DialogAdapter extends ArrayAdapter<Dialog> {

        public DialogAdapter(Context context, List<Dialog> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(getContext(), R.layout.list_item_dialog, null);
            }
            TextView dialogOwnerData = (TextView) convertView.findViewById(R.id.text_dialog_owner_data);
            TextView dialogTitleData = (TextView) convertView.findViewById(R.id.text_dialog_title_data);
            TextView dialogBodyData = (TextView) convertView.findViewById(R.id.text_dialog_body_data);
            TextView dialogUnreadMessageData = (TextView) convertView.findViewById(R.id.text_dialog_unread_message_data);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.image_dialog);

            Dialog dialog = getItem(position);
            Message message = dialog.getMessage();
            if (message==null) {return convertView;}
            User user = KnownUsers.getInstance().getUserFromId(message.getUserId());
            dialogOwnerData.setText(user.getFirstName()+" - "+user.getLastName());
            dialogTitleData.setText(dialog.getMessage().getTitle());
            dialogBodyData.setText(dialog.getMessage().getBody());
            dialogUnreadMessageData.setText("" + dialog.getUnread());
            ImageLoader.getInstance().displayImage(user.getPhoto100(),imageView);

            return convertView;
        }
    }


    private void registerAllNeededReceiver() {
        receiverDialog = new DialogReceiver();
        registerReceiver(receiverDialog, new IntentFilter(ApiFacadeService.BROADCAST_DIALOG));
        receiverDialogSearch = new DialogSearchReceiver();
        registerReceiver(receiverDialogSearch, new IntentFilter(ApiFacadeService.BROADCAST_MESSAGE_SEARCH));
        receiverUser = new UserReceiver();
        registerReceiver(receiverUser, new IntentFilter(ApiFacadeService.BROADCAST_USER));
    }

    private void unRegisterAllNeededReceiver() {
        if (receiverDialog!=null){
            unregisterReceiver(receiverDialog);
        }
        if (receiverDialogSearch!= null) {
            unregisterReceiver(receiverDialogSearch);
        }
        if (receiverUser!=null) {
            unregisterReceiver(receiverUser);
        }
    }

    private class DialogReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null ) {return;}
            Logger.log(TAG,"obtain dialogs from broadcast");
            ArrayList<Dialog> result;
            result = intent.getParcelableArrayListExtra(ApiFacadeService.BROADCAST_DIALOG);
            dialogAdapter.addAll(result);
            dialogAdapter.notifyDataSetChanged();
        }
    }

    private class DialogSearchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null ) {return;}
            Logger.log(TAG,"obtain searched message from broadcast");
            ArrayList<Message> resultMessage;
            resultMessage = intent.getParcelableArrayListExtra(ApiFacadeService.BROADCAST_MESSAGE_SEARCH);
            ArrayList<Dialog> resultDialogs = new ArrayList<Dialog>();
            Dialog tempDialog;
            dialogAdapter.clear();
            for (Message message : resultMessage){
                tempDialog = new Dialog(0,message);
                resultDialogs.add(tempDialog);
            }
            dialogAdapter.addAll(resultDialogs);
            dialogAdapter.notifyDataSetChanged();
            actionView.setIconified(true);
        }
    }

    private class UserReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.log(TAG,"obtain new user from broadcast");
            dialogAdapter.notifyDataSetChanged();
        }
    }
}
