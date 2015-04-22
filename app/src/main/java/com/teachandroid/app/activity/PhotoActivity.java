package com.teachandroid.app.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacade;
import com.teachandroid.app.api.SimpleResponseListener;
import com.teachandroid.app.data.Photo;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PhotoActivity extends ActionBarActivity {

    private PhotoAdapter photoAdapter;
    private GridView photoGrid;


    private boolean isMultyPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        photoAdapter = new PhotoAdapter(this, new ArrayList<Photo>());
        photoGrid = (GridView) findViewById(R.id.photo_view);
        photoGrid.setAdapter(photoAdapter);


        ApiFacade facade = new ApiFacade(this);

        facade.getPhotoFromProfile(new SimpleResponseListener<List<Photo>>() {
            @Override
            public void onResponse(final List<Photo> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        photoAdapter.addAll(response);
                    }
                });
                photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Photo photo = (Photo) parent.getItemAtPosition(position);

                        Intent intent = new Intent(PhotoActivity.this, CommentActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putParcelable("MY_PHOTO", photo);
                        intent.putExtras(bundle);

                        startActivity(intent);

                    }
                });

                //  isMultyPane = getResources().getBoolean(R.bool.is_multy_pane);


                //showDialogWithCommentsList(photoPosition);


            }
        });


    }

    final class PhotoAdapter extends ArrayAdapter<Photo> {

        public PhotoAdapter(Context context, List<Photo> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.grid_item_photo, null);
            }


            ImageView image = (ImageView) convertView.findViewById(R.id.imageviewPhoto);


            Photo photo = getItem(position);

            ImageLoader.getInstance().displayImage(photo.getPhoto604(), image);

            return convertView;
        }


    }


}
