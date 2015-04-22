package com.teachandroid.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacadeService;
import com.teachandroid.app.api.service.RequestForSend;
import com.teachandroid.app.data.Attachment;
import com.teachandroid.app.data.KnownUsers;
import com.teachandroid.app.data.Photo;
import com.teachandroid.app.data.Post;
import com.teachandroid.app.data.SavedPhoto;
import com.teachandroid.app.data.UpLoadData;
import com.teachandroid.app.data.User;
import com.teachandroid.app.util.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserActivity extends ActionBarActivity {

    private static final String TAG = UserActivity.class.getSimpleName();

    public static String EXTRA_USER_ID = "EXTRA_USER_ID";

    private static final int PHOTO_GET= 370;
    private static ImageButton addPhotoImageButton;
    private static String photoFile;
    private static UserActivity userActivity;

    private List<Long> knownUsersList = new ArrayList<Long>();

    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private PostReceiver receiverPost;
    private UpLoadServerReceiver receiverUpLoadServer;
    private SavedPhotoReceiver receiverSavedPhoto;
    private static PostAdapter postAdapter;
    private Intent intent;
    private Long userId;
    private static SavedPhoto infoAboutSavedPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            obtainDataFromIntent();
            initializeVariables();
            fillFieldsOnActivity();
            setAdapters();
            setListeners();
            if (savedInstanceState == null) {
                showFragment(userId);
            }
            registerAllNeededReceiver();
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
        userId = intent.getLongExtra(EXTRA_USER_ID,1);
        knownUsersList.addAll(KnownUsers.getInstance().getAllKnownUsers());
        userActivity=this;
    }

    private void fillFieldsOnActivity(){
        setContentView(R.layout.drawer_layout);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
    }

    private void setAdapters(){
        UserAdapter tempAdapter =  new UserAdapter(this, knownUsersList);
        drawerList.setAdapter(tempAdapter);

        postAdapter = new PostAdapter(this, new ArrayList<Post>());
    }

    private void setListeners(){
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Long sendId = (Long) parent.getItemAtPosition(position);
                RequestForSend.getInstance().sendMePosts(sendId);
                showFragment(sendId);
                drawerLayout.closeDrawer(drawerList);
            }
        });
    }
    private void showFragment(Long id){

        RequestForSend.getInstance().sendMePosts(id);
        Bundle sendBundle = getBundleWithParameters(id);
        Fragment openFragment = new UserFragment();
        openFragment.setArguments(sendBundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, openFragment)
                .commit();
        Logger.log(TAG,"show fragment with id "+id);
      }

    private Bundle getBundleWithParameters(Long id){
        Bundle resultBundle = new Bundle();
        resultBundle.putString(EXTRA_USER_ID, "" + id);
        return resultBundle;
    }

    private void registerAllNeededReceiver() {
        receiverPost = new PostReceiver();
        registerReceiver(receiverPost,new IntentFilter(ApiFacadeService.BROADCAST_POST));
        receiverUpLoadServer = new UpLoadServerReceiver();
        registerReceiver(receiverUpLoadServer, new IntentFilter(ApiFacadeService.BROADCAST_SERVER_FOR_UPLOAD));
        receiverSavedPhoto = new SavedPhotoReceiver();
        registerReceiver(receiverSavedPhoto,new IntentFilter(ApiFacadeService.BROADCAST_SAVED_MESSAGE_PHOTO));
    }

    private void unRegisterAllNeededReceiver() {
        if (receiverPost!=null ) {unregisterReceiver(receiverPost);}
        if (receiverUpLoadServer!=null ) {unregisterReceiver(receiverUpLoadServer);}
        if (receiverSavedPhoto!=null ) {unregisterReceiver(receiverSavedPhoto);}
    }

    // ******************** Class ************************************************


    private static final class UserAdapter extends ArrayAdapter<Long> {
        public UserAdapter(Context context, List<Long> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.list_item_friend, null);
            }
            ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_image100);
            ImageLoader.getInstance().displayImage(
                    KnownUsers.getInstance().getUserFromId(getItem(position)).getPhoto100(),
                    imageView);
            return convertView;
        }
    }

    private final class PostAdapter extends ArrayAdapter<Post>{

        public PostAdapter(Context context, List<Post> objects) {
            super(context, 0, objects);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Post post = getItem(position);
            TextView textTitle = new TextView(getContext());
            TextView textType = new TextView(getContext());

            if (convertView == null) {

                convertView = View.inflate(getContext(), R.layout.list_item_friend, null);
                textTitle = (TextView)convertView.findViewById(R.id.text_user_first_name);
                textType = (TextView)convertView.findViewById(R.id.text_user_last_name);

                if (post.getFirstAttachmentType().equals("photo")){
                    convertView = View.inflate(getContext(), R.layout.list_item_message, null);
                    textTitle = (TextView)convertView.findViewById(R.id.text_message_owner_data);
                    textType = (TextView)convertView.findViewById(R.id.text_message_body_data);

                }
                if (post.getFirstAttachmentType().equals("link")){
                    convertView = View.inflate(getContext(), R.layout.list_item_audio, null);
                    textTitle = (TextView)convertView.findViewById(R.id.text_artist);
                    textType = (TextView)convertView.findViewById(R.id.text_title);
                }
            }

            textTitle.setText(post.getText());
            textType.setText(post.getFirstAttachmentType());

            return convertView;
        }
    }

    private class PostReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null ) {return;}
            Logger.log(TAG,"obtain posts from broadcast");
            ArrayList<Post> result;
            result = intent.getParcelableArrayListExtra(ApiFacadeService.BROADCAST_POST);
            postAdapter.clear();
            postAdapter.addAll(result);
            postAdapter.notifyDataSetChanged();
        }
    }

    private class UpLoadServerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null ) {return;}
            Logger.log(TAG,"obtain Up Load Server from broadcast");
            ArrayList<UpLoadData> result;
            result = intent.getParcelableArrayListExtra(ApiFacadeService.BROADCAST_SERVER_FOR_UPLOAD);
            sendPostRequest1(result.get(0));
        }

        private void sendPostRequest1(final UpLoadData upLoadData) {
            final Executor requestExecutor = Executors.newFixedThreadPool(3);
            requestExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    String server ="";
                    String photo ="";
                    String hash ="";
                    try {
                        String uploadServer=upLoadData.getUploadUrl();
                        File fileToUpload = new File(photoFile);
                        HttpClient client=new DefaultHttpClient();
                        HttpPost httpPost=new HttpPost(uploadServer);
                        MultipartEntity albumArtEntity = new MultipartEntity();
                        albumArtEntity.addPart("photo", new FileBody(fileToUpload));
                        httpPost.setEntity(albumArtEntity);
                        HttpResponse response=client.execute(httpPost);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                        StringBuilder builder = new StringBuilder();
                        for (String line; (line = reader.readLine()) != null;) {
                            builder.append(line).append("\n");
                        }
                        JSONObject photoObject = new JSONObject(builder.toString());
                        server =photoObject.get("server").toString();
                        photo = photoObject.get("photo").toString();
                        hash = photoObject.get("hash").toString();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RequestForSend.getInstance().sendMeSavedPhoto(server,photo,hash);
                }
            });
        }

        private void sendPostRequest2(UpLoadData upLoadData) throws Exception{
            String urlToConnect = upLoadData.getUploadUrl();
            //content://media/external/images/media/27
            File fileToUpload = new File(Environment.getExternalStorageDirectory().toString()+"/DCIM/album_2012_monkey_me.jpg");

            String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.

            URLConnection connection = new URL(urlToConnect).openConnection();
            connection.setDoOutput(true); // This sets request method to POST.
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.println("--" + boundary);
                writer.println("Content-Disposition: form-data; file[]=\"picture\"; filename=\"1.png\"");
                writer.println("Content-Type: image/png");
                writer.println();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileToUpload)));
                    for (String line; (line = reader.readLine()) != null;) {
                        writer.println(line);
                    }
                } finally {
                    if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
                }
                writer.println("--" + boundary + "--");
            } finally {
                if (writer != null) writer.close();
            }

            // Connection is lazily executed whenever you request any status.
            int responseCode = ((HttpURLConnection) connection).getResponseCode();
            System.out.println(responseCode); // Should be 200

            InputStreamReader is = new InputStreamReader(connection.getInputStream(), "UTF-8");
            StringBuilder sb = new StringBuilder("");
            int bit;
            while ((bit = is.read()) != -1) sb.append((char)bit);
            is.close();

            System.out.println(sb);
        }
    }

    private class SavedPhotoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null ) {return;}
            Logger.log(TAG,"obtain data about Saved Photo from broadcast");
            ArrayList<SavedPhoto> result;
            result = intent.getParcelableArrayListExtra(ApiFacadeService.BROADCAST_SAVED_MESSAGE_PHOTO);
            infoAboutSavedPhoto = result.get(0);
        }
    }

    public static class UserFragment extends Fragment{

        private View userView;
        private Long userId;
        private User user;

        private ListView postList;

        public  UserFragment ( ) {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            userView = inflater.inflate(R.layout.activity_user, container, false);
            userId = Long.parseLong(getArguments().getString(EXTRA_USER_ID));
            user = KnownUsers.getInstance().getUserFromId(userId);

            fillFieldOnActivity();
            return userView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode== RESULT_OK) {
                if (requestCode==PHOTO_GET){
                    Uri selectedImageUri = data.getData();

                    String tempPath = getPath(selectedImageUri, userActivity);
                    photoFile = tempPath;
                    Bitmap bitMap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitMap = BitmapFactory.decodeFile(tempPath, bitmapOptions);
                    addPhotoImageButton.setImageBitmap(bitMap);
                    RequestForSend.getInstance().sendMeServerForUploadPhoto();
                }
            }
        }

        private String getPath(Uri uri, Activity activity) {
            String[] projection = { MediaStore.MediaColumns.DATA };
            Cursor cursor = activity
                    .managedQuery(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        private void fillFieldOnActivity(){
            ImageView imageView = (ImageView)userView.findViewById(R.id.image_user);
            ImageLoader.getInstance().displayImage(user.getPhoto100(),imageView);

            postList = (ListView) userView.findViewById(R.id.list_post);
            postList.setAdapter(postAdapter);
            postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Post post = (Post)parent.getItemAtPosition(position);
                    if (post == null ) {return;}
                    Attachment attachment = post.getFirstAttachment();
                    if (attachment.getType().equals("photo")) {
                        Photo photo = attachment.getPhoto();

                        Intent intent = new Intent(view.getContext(), CommentActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putParcelable("MY_PHOTO", photo);
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }
                }
            });


            Button buttonSend = (Button)userView.findViewById(R.id.button_send_message);
            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String attachment = "";
                    EditText editText = (EditText)userView.findViewById(R.id.edit_send_message);
                    if (editText.getText().toString().equals("")) {return;}
                    if (infoAboutSavedPhoto!= null) {
                        attachment = "photo"+infoAboutSavedPhoto.getOwnerId()+"_"+infoAboutSavedPhoto.getId();
                    }
                    RequestForSend.getInstance().sendMessageWithoutReturnData(user.getId(),
                                                editText.getText().toString(),
                                                attachment,
                                                RequestForSend.TYPE_OF_RECIPIENT_USER);
                    editText.setText("");

                    RequestForSend.getInstance().sendNotification(
                            getString(R.string.text_send_message_to) + user.getFirstName() + " " + user.getLastName(),
                            editText.getText().toString());
                }
            });
            ImageButton buttonAddPhoto = (ImageButton)userView.findViewById(R.id.button_add_photo);
            addPhotoImageButton = buttonAddPhoto;
            buttonAddPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPhoto(v.getContext());
                }
            });
        }
        private void addPhoto(final Context context){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setItems(R.array.add_photo_for_send, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position) {
                    String[] strings = getResources().getStringArray(R.array.add_photo_for_send);
                    if (position==0) {
                        Toast.makeText(context,strings[position],Toast.LENGTH_SHORT).show();
                        functionAddPhotoMustBeRenamed();
                    }
                    if (position==1) {
                        Toast.makeText(context,strings[position],Toast.LENGTH_SHORT).show();
                    }
                    if (position==2) {
                        Toast.makeText(context,strings[position],Toast.LENGTH_SHORT).show();
                    }
                }
            });
            final AlertDialog ld = builder.create();
            ld.show();
        }

        private void functionAddPhotoMustBeRenamed(){
            //1 - отправить запрос
            //2  - отправить фото
            //3 - отправить еще че-то
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"),PHOTO_GET);

        }
    }
}
