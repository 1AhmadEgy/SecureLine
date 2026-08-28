package com.secureline.secureline.security;

import android.app.Notification;
import android.content.Context;
import android.os.Build;

public class NotificationSecurity {

    public static void applySecureNotificationSettings(Notification notification, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.visibility = Notification.VISIBILITY_SECRET;
            notification.publicVersion = new Notification.Builder(context)
                .setContentTitle("SecureLine")
                .setContentText("New encrypted message")
                .build();
        }
    }

    public static boolean shouldHideNotificationContent() {
        return true;
    }
}
