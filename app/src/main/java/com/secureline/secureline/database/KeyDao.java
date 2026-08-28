package com.secureline.secureline.database;

import android.content.ContentValues;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

public class KeyDao {

    private final SQLiteDatabase db;

    public KeyDao(SQLiteDatabase database) {
        this.db = database;
    }

    public long insertKey(String keyAlias, String keyData, long createdAt) {
        ContentValues values = new ContentValues();
        values.put("key_alias", keyAlias);
        values.put("key_data", keyData);
        values.put("created_at", createdAt);
        return db.insert("keys", null, values);
    }

    public String getKeyData(String keyAlias) {
        android.database.Cursor cursor = db.query(
            "keys",
            new String[]{"key_data"},
            "key_alias = ?",
            new String[]{keyAlias},
            null, null, null
        );

        String keyData = null;
        if (cursor.moveToFirst()) {
            keyData = cursor.getString(0);
        }
        cursor.close();
        return keyData;
    }

    public void updateKey(String keyAlias, String newKeyData) {
        ContentValues values = new ContentValues();
        values.put("key_data", newKeyData);
        db.update("keys", values, "key_alias = ?", new String[]{keyAlias});
    }

    public void deleteKey(String keyAlias) {
        db.delete("keys", "key_alias = ?", new String[]{keyAlias});
    }
}
