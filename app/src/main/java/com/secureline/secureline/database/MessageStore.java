package com.secureline.secureline.database;

import com.secureline.secureline.security.KeyManager;

import net.zetetic.database.sqlcipher.SQLiteDatabase;
import net.zetetic.database.sqlcipher.SQLiteOpenHelper;

import android.content.Context;

public class MessageStore extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "secureline_messages.db";
    private static final int DATABASE_VERSION = 1;

    static {
        System.loadLibrary("sqlcipher");
    }

    public MessageStore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS messages (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "conversation_id TEXT NOT NULL, " +
            "sender_id TEXT NOT NULL, " +
            "body TEXT NOT NULL, " +
            "timestamp INTEGER NOT NULL, " +
            "encrypted INTEGER DEFAULT 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS messages");
        onCreate(db);
    }

    public SQLiteDatabase getSecureDatabase() {
        String key = KeyManager.getDatabaseKeyAsString();
        if (key == null) {
            return null;
        }
        return this.getWritableDatabase(key);
    }
}
