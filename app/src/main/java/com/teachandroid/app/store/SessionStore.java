package com.teachandroid.app.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import com.teachandroid.app.data.Session;

import java.net.URLDecoder;

public class SessionStore {

    private static final String KEY = "m-session";

    public static Session restore(Context context) {
        Session session = new Session();
        SharedPreferences prefs = context.getSharedPreferences(KEY,
                Context.MODE_PRIVATE);
        session.setUserId(prefs.getString("user_id", null));
        session.setAccessToken(prefs.getString("access_token", null));
        session.setExpireIn(prefs.getString("expires_in", null));
        return session;
    }

    public static long getOwnerId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(KEY,
                Context.MODE_PRIVATE);
        return Long.parseLong(prefs.getString("user_id", ""));
    }


    public static boolean isOwner(long userId, Context context) {
        boolean result = false;
        SharedPreferences prefs = context.getSharedPreferences(KEY,
                Context.MODE_PRIVATE);
        String mid = prefs.getString("user_id", null);
        if (mid != null) {
            result = userId == Long.parseLong(mid);
        }
        return result;
    }

    public static void reLogin(Context context) {
        context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit().clear().commit();
    }

    public static boolean isValidSession(Context context){
        return restore(context).isValid();
    }
    
    public static void parseSession(String url,Context context) {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
                        .edit();
        int findIndex = url.indexOf("#");
        String sessionStr = URLDecoder.decode(url.substring(findIndex + 1, url.length()));
        String[] paramStrings = sessionStr.split("&");
        for (String param : paramStrings) {
            String items[] = param.split("=");
            editor.putString(items[0], items[1]);
        }
        editor.commit();
    }

    public static Bundle parseError(String url) {
        Bundle params = new Bundle();
        int findIndex = url.indexOf("?");
        String errorStr = url.substring(findIndex + 1, url.length());
        String[] paramStrings = errorStr.split("&");
        for (String param : paramStrings) {
            String items[] = param.split("=");
            params.putString(items[0], items[1].replaceAll("+", " "));
        }
        return params;
    }
}
