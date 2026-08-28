package com.secureline.secureline.database;

import com.secureline.secureline.security.KeyManager;

import net.zetetic.database.sqlcipher.SQLiteDatabase;
import net.zetetic.database.sqlcipher.SQLiteOpenHelper;

import android.content.Context;

public class ContactStore extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "secureline_contacts.db";
    private static final int DATABASE_VERSION = 1;

    static {
        System.loadLibrary("sqlcipher");
    }

    public ContactStore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS contacts (" +
            "id TEXT PRIMARY KEY, " +
            "display_name TEXT, " +
            "public_key TEXT NOT NULL, " +
            "verified INTEGER DEFAULT 0, " +
            "added_timestamp INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
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
