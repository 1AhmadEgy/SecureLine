package com.secureline.secureline.security;

import android.app.Activity;
import android.view.WindowManager;

public class SecurityHardening {

    public static void applyAllHardening(Activity activity) {
        ScreenshotProtection.enable(activity);
        activity.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );
    }

    public static void removeHardening(Activity activity) {
        ScreenshotProtection.disable(activity);
    }
}
