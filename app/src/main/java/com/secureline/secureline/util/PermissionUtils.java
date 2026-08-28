package com.secureline.secureline.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {

    public static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static boolean hasAudioPermissions(Context context) {
        return hasPermission(context, Manifest.permission.RECORD_AUDIO) &&
               hasPermission(context, Manifest.permission.MODIFY_AUDIO_SETTINGS);
    }

    public static boolean hasCameraPermission(Context context) {
        return hasPermission(context, Manifest.permission.CAMERA);
    }

    public static boolean hasStoragePermissions(Context context) {
        return hasPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) &&
               hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static String[] getCallPermissions() {
        return new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        };
    }

    public static String[] getQrPermissions() {
        return new String[]{
            Manifest.permission.CAMERA
        };
    }
}
