package com.secureline.secureline.security;

import android.app.AlertDialog;
import android.content.Context;

public class SecureAlertDialog {

    public static void show(Context context, String title, String message) {
        new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("موافق", (dialog, which) -> dialog.dismiss())
            .setCancelable(false)
            .show();
    }

    public static void showConfirm(Context context, String title, String message, 
                                    Runnable onConfirm) {
        new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("تأكيد", (dialog, which) -> {
                if (onConfirm != null) onConfirm.run();
            })
            .setNegativeButton("إلغاء", (dialog, which) -> dialog.dismiss())
            .setCancelable(false)
            .show();
    }
}
