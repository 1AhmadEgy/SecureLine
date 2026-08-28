package com.secureline.secureline.security;

import android.app.Activity;
import android.view.WindowManager;

public class ScreenshotProtection {

    public static void enable(Activity activity) {
        activity.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );
    }

    public static void disable(Activity activity) {
        activity.getWindow().clearFlags(
            WindowManager.LayoutParams.FLAG_SECURE
        );
    }
}
