package com.secureline.secureline.database;

import android.content.Context;

import com.secureline.secureline.security.KeyManager;

import net.zetetic.database.sqlcipher.SQLiteDatabase;
import net.zetetic.database.sqlcipher.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DB_NAME = "secureline.db";
    private static final int DB_VERSION = 4;

    static {
        System.loadLibrary("sqlcipher");
    }

    private static DatabaseManager instance;
    private final Context appContext;

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.appContext = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DatabaseInitializer.initialize(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DatabaseMigration.migrate(db, oldVersion, newVersion);
    }

    public SQLiteDatabase getSecureDatabase() {
        String key = KeyManager.getDatabaseKeyAsString(appContext);
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
