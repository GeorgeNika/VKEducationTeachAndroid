package com.teachandroid.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.*;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.teachandroid.app.R;
import com.teachandroid.app.store.SessionStore;
import com.teachandroid.app.util.Logger;

import java.util.regex.Pattern;

//TODO : http://vk.com/dev/auth_mobile
public class LoginActivity extends Activity {

    private static final String AUTH_URI = "https://oauth.vk.com/authorize?";

    private static final String PARAM_CLIENT_ID = "client_id=4798286";

    private static final String SCOPE = "scope=notify,friends,photos,audio,video,docs,notes,pages,offers,questions,wall,group,messages,offline";

    private static final String REDIRECT_URI = "redirect_uri=http://oauth.vk.com/blank.html";

    private static final String DISPLAY = "display=touch";

    private static final String RESPONSE_TYPE = "response_type=token";

    private static final String VERSION = "v=5.28";

    private static final String REQUEST_STR = AUTH_URI + PARAM_CLIENT_ID + "&"
            + SCOPE + "&" + REDIRECT_URI + "&" + DISPLAY + "&" + RESPONSE_TYPE + "&" + VERSION;

    private static final String TAG = LoginActivity.class.getSimpleName();

    private WebView loginWebView;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SessionStore.isValidSession(this)) {
            setContentView(R.layout.activity_login);
            loginWebView = (WebView) findViewById(R.id.web_view);
            WebSettings webSettings = loginWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setSupportZoom(false);
            webSettings.setSaveFormData(true);
            loginWebView.requestFocus(View.FOCUS_DOWN);
            loginWebView.setWebViewClient(new VkClient());
            loginWebView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_UP:
                            if (!view.hasFocus()) {
                                view.requestFocus();
                            }
                            break;
                    }
                    return false;
                }
            });
            loginWebView.loadUrl(REQUEST_STR);
        } else {
            setContentView(R.layout.activity_splash);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }, 1500);
        }
    }

    private class VkClient extends WebViewClient {

        private final Pattern pattern = Pattern.compile("https?://(api|oauth)\\.vk\\.com/blank\\.html.*");

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Logger.log(TAG, "redirect url " + url);
            if (pattern.matcher(url).matches()) {
                if (url.indexOf("#") > 0) {
                    SessionStore.parseSession(url, LoginActivity.this);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                } else {
                    SessionStore.parseError(url);
                }
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
