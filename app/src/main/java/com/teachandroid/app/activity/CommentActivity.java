package com.teachandroid.app.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacade;
import com.teachandroid.app.api.SimpleResponseListener;
import com.teachandroid.app.api.reponse.ApiResponse;
import com.teachandroid.app.api.reponse.ResponseList;
import com.teachandroid.app.data.Comment;
import com.teachandroid.app.data.Friend;
import com.teachandroid.app.data.Photo;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends ActionBarActivity {

    private CommentAdapter commentAdapter;
    private ListView commentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        commentAdapter = new CommentAdapter(this, new ArrayList<Comment>());
        commentList = (ListView) findViewById(R.id.comments_list);
        commentList.setAdapter(commentAdapter);

        ApiFacade facade = new ApiFacade(this);

        Intent intent = getIntent();

        Photo somePhoto = (Photo) intent.getParcelableExtra("MY_PHOTO");

        String TAG = CommentActivity.class.getSimpleName();
        Log.d(TAG, somePhoto.toString());


        ImageView photoOrig = (ImageView) findViewById(R.id.photo);

        ImageLoader.getInstance().displayImage(somePhoto.getPhoto807(), photoOrig);


        facade.getCommentsPhoto(somePhoto, new SimpleResponseListener<List<Comment>>() {
            @Override
            public void onResponse(final List<Comment> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        commentAdapter.addAll(response);
                    }
                });
            }
        });
    }


    public final class CommentAdapter extends ArrayAdapter<Comment> {

        public CommentAdapter(Context context, List<Comment> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.list_item_comment, null);
            }


            TextView idView = (TextView) convertView.findViewById(R.id.text_comment_id);
            TextView fromId = (TextView) convertView.findViewById(R.id.text_from_id);
            TextView textMessage = (TextView) convertView.findViewById(R.id.text_message);

            Comment comment = getItem(position);

            idView.setText(Long.toString(comment.getId()));
            fromId.setText(Long.toString(comment.getFrom_id()));
            textMessage.setText(comment.getText());

            return convertView;

        }
    }
}
