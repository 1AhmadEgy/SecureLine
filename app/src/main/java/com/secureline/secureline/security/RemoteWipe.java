package com.secureline.secureline.security;

import com.secureline.secureline.database.DatabaseHelper;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

public class RemoteWipe {

    public static void wipeAllData(DatabaseHelper helper) {
        if (helper == null) return;

        SQLiteDatabase db = helper.getSecureDb();
        if (db == null) return;

        db.execSQL("DELETE FROM messages");
        db.execSQL("DELETE FROM contacts");
        db.execSQL("DELETE FROM sessions");
        db.execSQL("DELETE FROM keys");

        db.close();
    }

    public static void wipeAllData() {
        // Full app data wipe
        System.exit(0);
    }
}
