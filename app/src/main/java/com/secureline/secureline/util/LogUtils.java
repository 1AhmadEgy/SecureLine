package com.secureline.secureline.util;

import android.util.Log;

public class LogUtils {

    private static final String TAG = "SecureLine";
    private static boolean debugMode = false;

    public static void setDebugMode(boolean enabled) {
        debugMode = enabled;
    }

    public static void d(String message) {
        if (debugMode) Log.d(TAG, message);
    }

    public static void i(String message) {
        if (debugMode) Log.i(TAG, message);
    }

    public static void w(String message) {
        if (debugMode) Log.w(TAG, message);
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void e(String message, Throwable throwable) {
        Log.e(TAG, message, throwable);
    }

    public static void security(String message) {
        Log.w(TAG, "[SECURITY] " + message);
    }
}
