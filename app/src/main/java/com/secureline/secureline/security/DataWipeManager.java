package com.secureline.secureline.security;

import android.content.Context;

import com.secureline.secureline.database.DatabaseManager;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

public class DataWipeManager {

    public static void wipeAllData(Context context) {
        wipeDatabase();
        wipePreferences(context);
        wipeFiles(context);
        wipeCache(context);
    }

    private static void wipeDatabase() {
        DatabaseManager manager = DatabaseManager.getInstance(null);
        if (manager != null) {
            SQLiteDatabase db = manager.getSecureDatabase();
            if (db != null) {
                db.execSQL("DELETE FROM messages");
                db.execSQL("DELETE FROM contacts");
                db.execSQL("DELETE FROM sessions");
                db.execSQL("DELETE FROM keys");
                db.execSQL("DELETE FROM conversations");
                db.execSQL("DELETE FROM groups");
                db.execSQL("DELETE FROM group_members");
                db.close();
            }
        }
    }

    private static void wipePreferences(Context context) {
        context.getSharedPreferences("secureline_prefs", Context.MODE_PRIVATE)
               .edit().clear().apply();
        context.getSharedPreferences("secureline_sessions", Context.MODE_PRIVATE)
               .edit().clear().apply();
    }

    private static void wipeFiles(Context context) {
        java.io.File filesDir = context.getFilesDir();
        if (filesDir != null && filesDir.exists()) {
            for (java.io.File file : filesDir.listFiles()) {
                file.delete();
            }
        }
    }

    private static void wipeCache(Context context) {
        java.io.File cacheDir = context.getCacheDir();
        if (cacheDir != null && cacheDir.exists()) {
            for (java.io.File file : cacheDir.listFiles()) {
                file.delete();
            }
        }
    }
}
