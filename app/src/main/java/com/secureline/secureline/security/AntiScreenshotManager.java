package com.secureline.secureline.security;

import android.app.Activity;
import android.view.WindowManager;

public class AntiScreenshotManager {

    public static void blockScreenshots(Activity activity) {
        activity.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );
    }

    public static void allowScreenshots(Activity activity) {
        activity.getWindow().clearFlags(
            WindowManager.LayoutParams.FLAG_SECURE
        );
    }
}
