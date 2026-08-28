package com.secureline.secureline.util;

import android.util.Log;

public class Logger {

    private static final String TAG = "SecureLine";
    private static boolean enabled = true;

    public static void setEnabled(boolean isEnabled) {
        enabled = isEnabled;
    }

    public static void d(String message) {
        if (enabled) Log.d(TAG, message);
    }

    public static void i(String message) {
        if (enabled) Log.i(TAG, message);
    }

    public static void w(String message) {
        if (enabled) Log.w(TAG, message);
    }

    public static void e(String message) {
        if (enabled) Log.e(TAG, message);
    }

    public static void e(String message, Throwable throwable) {
        if (enabled) Log.e(TAG, message, throwable);
    }
}
