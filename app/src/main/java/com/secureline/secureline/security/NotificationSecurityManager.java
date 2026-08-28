package com.secureline.secureline.security;

import android.app.Notification;
import android.content.Context;
import android.os.Build;

public class NotificationSecurityManager {

    public static void applySecureNotification(Notification notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.visibility = Notification.VISIBILITY_SECRET;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notification.publicVersion = null;
        }
    }

    public static boolean isNotificationSecure() {
        return true;
    }
}
