package com.teachandroid.app.util;

import android.util.Log;

public class Logger {

    private static final String TAG = "TeachAndroid";

    public static void log(String tag, String format){
        Log.d(String.format("%s - %s", TAG, tag), format);
    }
}
