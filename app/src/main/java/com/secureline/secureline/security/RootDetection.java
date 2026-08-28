package com.secureline.secureline.security;

import java.io.File;

public class RootDetection {

    public static boolean isDeviceRooted() {
        return checkSuBinary() || checkMagiskBinary() || checkSuperUserApk();
    }

    private static boolean checkSuBinary() {
        String[] paths = {
            "/system/bin/su",
            "/system/xbin/su",
            "/sbin/su",
            "/su/bin/su"
        };
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkMagiskBinary() {
        String[] paths = {
            "/sbin/magisk",
            "/data/adb/magisk"
        };
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkSuperUserApk() {
        String[] packages = {
            "com.noshufou.android.su",
            "com.thirdparty.superuser",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser"
        };
        for (String pkg : packages) {
            try {
                java.lang.Runtime.getRuntime().exec("pm list packages " + pkg);
                return true;
            } catch (Exception e) {
                // Ignore
            }
        }
        return false;
    }
}
