package com.teachandroid.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.teachandroid.app.R;
import com.teachandroid.app.store.SessionStore;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_audio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AudioAlbumsActivity.class));
            }
        });

        findViewById(R.id.button_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FriendActivity.class));
            }
        });
        findViewById(R.id.button_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DialogActivity.class));
            }
        });
        findViewById(R.id.groups_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GroupsActivity.class));
            }
        });

        findViewById(R.id.button_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PhotoActivity.class));
            }
        });
        findViewById(R.id.button_relogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionStore.reLogin(MainActivity.this);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        findViewById(R.id.button_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VideoActivity.class));
            }
        });
    }
}
