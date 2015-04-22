package com.teachandroid.app.activity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacade;
import com.teachandroid.app.api.SimpleResponseListener;
import com.teachandroid.app.data.Group;
import com.teachandroid.app.data.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends ActionBarActivity {

    private VideoAdapter videoAdapter;

    private ListView videoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoAdapter = new VideoAdapter(this, new ArrayList<Video>());

        videoList = (ListView) findViewById(R.id.video_list);

        videoList.setAdapter(videoAdapter);

        ApiFacade facade = new ApiFacade(this);

        facade.getVideo(new SimpleResponseListener<List<Video>>(){
            @Override
            public void onResponse(final List<Video> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        videoAdapter.addAll(response);
                    }
                });
            }
        });
    }
    private final class VideoAdapter extends ArrayAdapter<Video> {

        public VideoAdapter(Context context, List<Video> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(getContext(), R.layout.video_list, null);
            }
            TextView titleView = (TextView) convertView.findViewById(R.id.text_video);
            ImageView artistView = (ImageView) convertView.findViewById(R.id.photo_video);

            Video item = getItem(position);

            titleView.setText(item.getTitle());


            ImageLoader.getInstance().displayImage(item.getPhoto_130(), artistView);

            return convertView;
        }
    }




}
