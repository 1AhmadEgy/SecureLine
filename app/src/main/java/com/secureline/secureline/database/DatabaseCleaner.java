package com.secureline.secureline.database;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

public class DatabaseCleaner {

    public static void cleanOldMessages(SQLiteDatabase db, long olderThanMillis) {
        db.execSQL("DELETE FROM messages WHERE timestamp < ?", 
            new Object[]{olderThanMillis});
    }

    public static void cleanReadMessages(SQLiteDatabase db) {
        db.execSQL("DELETE FROM messages WHERE is_read = 1");
    }

    public static void cleanAllMessages(SQLiteDatabase db) {
        db.execSQL("DELETE FROM messages");
    }

    public static void cleanDeletedMessages(SQLiteDatabase db) {
        db.execSQL("DELETE FROM messages WHERE is_deleted = 1");
    }

    public static void vacuumDatabase(SQLiteDatabase db) {
        db.execSQL("VACUUM");
    }
}