package com.secureline.secureline.security;

import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class SecurityScanner {

    public static List<String> scanForThreats(Context context) {
        List<String> threats = new ArrayList<>();

        if (RootDetection.isDeviceRooted()) {
            threats.add("Device is rooted");
        }

        if (EmulatorDetection.isEmulator()) {
            threats.add("Running on emulator");
        }

        if (AntiDebugging.isDebuggerConnected()) {
            threats.add("Debugger connected");
        }

        if (AntiDebugging.isBeingTraced()) {
            threats.add("Process being traced");
        }

        if (hasSuspiciousPackages(context)) {
            threats.add("Suspicious packages detected");
        }

        return threats;
    }

    private static boolean hasSuspiciousPackages(Context context) {
        String[] suspiciousPackages = {
            "com.saurik.substrate",
            "de.robv.android.xposed.installer",
            "com.chelpus.luckypatcher"
        };

        PackageManager pm = context.getPackageManager();
        for (String pkg : suspiciousPackages) {
            try {
                pm.getPackageInfo(pkg, 0);
                return true;
            } catch (Exception e) {
                // Not installed
            }
        }
        return false;
    }
}
