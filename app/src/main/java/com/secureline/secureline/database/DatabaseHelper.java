package com.secureline.secureline.database;

import com.secureline.secureline.security.KeyManager;

import net.zetetic.database.sqlcipher.SQLiteDatabase;
import net.zetetic.database.sqlcipher.SQLiteOpenHelper;

import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "secureline.db";
    private static final int DB_VERSION = 1;

    static {
        System.loadLibrary("sqlcipher");
    }

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE sessions (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "session_id TEXT UNIQUE, " +
            "created_at INTEGER)");

        db.execSQL("CREATE TABLE messages (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "message_uuid TEXT UNIQUE, " +
            "conversation_uuid TEXT, " +
            "sender_uuid TEXT, " +
            "body TEXT, " +
            "timestamp INTEGER, " +
            "is_read INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE contacts (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "contact_uuid TEXT UNIQUE, " +
            "display_name TEXT, " +
            "public_key TEXT, " +
            "is_verified INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE keys (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "key_alias TEXT UNIQUE, " +
            "key_data TEXT, " +
            "created_at INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS sessions");
        db.execSQL("DROP TABLE IF EXISTS messages");
        db.execSQL("DROP TABLE IF EXISTS contacts");
        db.execSQL("DROP TABLE IF EXISTS keys");
        onCreate(db);
    }

    public SQLiteDatabase getSecureDb() {
        String key = KeyManager.getDatabaseKeyAsString();
        return getWritableDatabase(key);
    }
}
