package com.secureline.secureline.security;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class PanicButton {

    public static void triggerPanicWipe(Context context) {
        // Wipe local data
        SessionManager sessionManager = new SessionManager(context);
        sessionManager.clearSession();

        // Clear app data
        context.getCacheDir().delete();
        context.getFilesDir().delete();

        // Restart app in clean state
        Intent intent = context.getPackageManager()
            .getLaunchIntentForPackage(context.getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | 
                           Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Runtime.getRuntime().exit(0);
        }
    }
}
