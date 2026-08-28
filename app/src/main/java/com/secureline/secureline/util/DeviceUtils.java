package com.secureline.secureline.util;

import android.os.Build;
import android.provider.Settings;

import android.content.Context;

public class DeviceUtils {

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(
            context.getContentResolver(),
            Settings.Secure.ANDROID_ID
        );
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static int getApiLevel() {
        return Build.VERSION.SDK_INT;
    }

    public static String getDeviceInfo() {
        return "Model: " + getDeviceModel() + 
               ", Manufacturer: " + getDeviceManufacturer() + 
               ", OS: " + getOsVersion() + 
               ", API: " + getApiLevel();
    }
}
