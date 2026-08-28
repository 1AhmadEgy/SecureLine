package com.secureline.secureline.database;

import android.content.Context;

import com.secureline.secureline.security.KeyManager;

import net.zetetic.database.sqlcipher.SQLiteDatabase;
import net.zetetic.database.sqlcipher.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DB_NAME = "secureline.db";
    private static final int DB_VERSION = 1;

    static {
        System.loadLibrary("sqlcipher");
    }

    private static DatabaseManager instance;

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    private DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DatabaseInitializer.initialize(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS sessions");
        db.execSQL("DROP TABLE IF EXISTS messages");
        db.execSQL("DROP TABLE IF EXISTS contacts");
        db.execSQL("DROP TABLE IF EXISTS keys");
        db.execSQL("DROP TABLE IF EXISTS conversations");
        db.execSQL("DROP TABLE IF EXISTS groups");
        db.execSQL("DROP TABLE IF EXISTS group_members");
        onCreate(db);
    }

    public SQLiteDatabase getSecureDatabase() {
        String key = KeyManager.getDatabaseKeyAsString();
        if (key == null) return null;
        return getWritableDatabase(key);
    }

    public MessageDao getMessageDao() {
        return new MessageDao(getSecureDatabase());
    }

    public ContactDao getContactDao() {
        return new ContactDao(getSecureDatabase());
    }

    public SessionDao getSessionDao() {
        return new SessionDao(getSecureDatabase());
    }

    public KeyDao getKeyDao() {
        return new KeyDao(getSecureDatabase());
    }

    public ConversationDao getConversationDao() {
        return new ConversationDao(getSecureDatabase());
    }

    public GroupDao getGroupDao() {
        return new GroupDao(getSecureDatabase());
    }
}
