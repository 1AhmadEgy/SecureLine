package com.secureline.secureline.security;

import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardProtection {

    public static void clearClipboard(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.clearPrimaryClip();
        }
    }

    public static void disableClipboardAccess(Context context) {
        clearClipboard(context);
    }
}
