package com.secureline.secureline.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showSuccess(Context context, String message) {
        Toast.makeText(context, "✓ " + message, Toast.LENGTH_SHORT).show();
    }

    public static void showError(Context context, String message) {
        Toast.makeText(context, "✗ " + message, Toast.LENGTH_SHORT).show();
    }

    public static void showSecurity(Context context, String message) {
        Toast.makeText(context, "🔒 " + message, Toast.LENGTH_SHORT).show();
    }
}
