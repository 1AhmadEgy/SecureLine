package com.secureline.secureline.network;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class PushNotificationManager {

    private static final String CHANNEL_ID = "secureline_messages";
    private final Context context;
    private final NotificationManager notificationManager;

    public PushNotificationManager(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "SecureLine Messages",
                NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("إشعارات الرسائل المشفرة");
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showMessageNotification(String sender, String messagePreview) {
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(sender)
            .setContentText("رسالة مشفرة جديدة")
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setAutoCancel(true)
            .build();

        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }

    public void showCallNotification(String callerName) {
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("مكالمة واردة")
            .setContentText(callerName)
            .setSmallIcon(android.R.drawable.ic_menu_call)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setOngoing(true)
            .build();

        notificationManager.notify(2000, notification);
    }

    public void cancelNotification(int notificationId) {
        notificationManager.cancel(notificationId);
    }

    public void cancelAllNotifications() {
        notificationManager.cancelAll();
    }
}
