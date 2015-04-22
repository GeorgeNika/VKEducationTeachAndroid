package com.teachandroid.app.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacade;
import com.teachandroid.app.api.SimpleResponseListener;
import com.teachandroid.app.data.Group;
import com.teachandroid.app.data.Video;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends Activity {
    private GroupAdapter groupAdapter;

    private ListView groupList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        groupAdapter = new GroupAdapter(this, new ArrayList<Group>());

        groupList = (ListView) findViewById(R.id.groups_list);

        groupList.setAdapter(groupAdapter);

        ApiFacade facade = new ApiFacade(this);
        facade.getGroups(new SimpleResponseListener<List<Group>>(){
            @Override
            public void onResponse(final List<Group> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        groupAdapter.addAll(response);
                    }
                });
            }
        });
    }
    private final class GroupAdapter extends ArrayAdapter<Group> {

        public GroupAdapter(Context context, List<Group> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(getContext(), R.layout.groups_list, null);
            }
            TextView titleView = (TextView) convertView.findViewById(R.id.text_group);
            ImageView artistView = (ImageView) convertView.findViewById(R.id.photo_group);

            Group item = getItem(position);

            titleView.setText(item.getName());

           // ImageSize targetSize = new ImageSize(80, 50);

            ImageLoader.getInstance().displayImage(item.getPhoto(),artistView);
            //artistView.setText(item.getArtist());
            return convertView;
        }
    }


}
