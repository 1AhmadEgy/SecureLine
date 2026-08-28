package com.secureline.secureline.database;

import android.content.ContentValues;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

public class SessionDao {

    private final SQLiteDatabase db;

    public SessionDao(SQLiteDatabase database) {
        this.db = database;
    }

    public long insertSession(String sessionId, long createdAt) {
        ContentValues values = new ContentValues();
        values.put("session_id", sessionId);
        values.put("created_at", createdAt);
        return db.insert("sessions", null, values);
    }

    public String getSessionId() {
        android.database.Cursor cursor = db.query(
            "sessions",
            new String[]{"session_id"},
            null, null, null, null,
            "created_at DESC",
            "1"
        );

        String sessionId = null;
        if (cursor.moveToFirst()) {
            sessionId = cursor.getString(0);
        }
        cursor.close();
        return sessionId;
    }

    public void deleteSession(String sessionId) {
        db.delete("sessions", "session_id = ?", new String[]{sessionId});
    }

    public void deleteAllSessions() {
        db.delete("sessions", null, null);
    }
}
