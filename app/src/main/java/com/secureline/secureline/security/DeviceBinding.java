package com.secureline.secureline.security;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

public class DeviceBinding {

    public static String getDeviceIdentifier(Context context) {
        String androidId = Settings.Secure.getString(
            context.getContentResolver(),
            Settings.Secure.ANDROID_ID
        );
        String hardwareSerial = Build.getSerial();
        return HashUtils.sha256Hex((androidId + hardwareSerial).getBytes());
    }

    public static boolean isDeviceBound(Context context, String expectedId) {
        String currentId = getDeviceIdentifier(context);
        return currentId != null && currentId.equals(expectedId);
    }
}
