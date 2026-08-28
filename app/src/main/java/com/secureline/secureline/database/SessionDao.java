package com.secureline.secureline.database;

import android.content.ContentValues;
import android.database.Cursor;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

import java.util.UUID;

public class SessionDao {

    private final SQLiteDatabase db;

    public SessionDao(SQLiteDatabase database) {
        this.db = database;
    }

    public String createSession() {
        String sessionId = UUID.randomUUID().toString();
        ContentValues values = new ContentValues();
        values.put("session_id", sessionId);
        values.put("created_at", System.currentTimeMillis());
        db.insert("sessions", null, values);
        return sessionId;
    }

    public String getCurrentSession() {
        Cursor cursor = db.query(
            "sessions",
            new String[]{"session_id"},
            null, null, null, null,
            "created_at DESC",
            "1"
        );
        String sessionId = null;
        if (cursor != null && cursor.moveToFirst()) {
            sessionId = cursor.getString(0);
        }
        if (cursor != null) cursor.close();
        return sessionId;
    }

    public boolean isValidSession(String sessionId) {
        Cursor cursor = db.query(
            "sessions",
            new String[]{"session_id"},
            "session_id = ?",
            new String[]{sessionId},
            null, null, null
        );
        boolean valid = cursor != null && cursor.moveToFirst();
        if (cursor != null) cursor.close();
        return valid;
    }

    public void deleteSession(String sessionId) {
        db.delete("sessions", "session_id = ?", new String[]{sessionId});
    }

    public void deleteAllSessions() {
        db.delete("sessions", null, null);
    }
}
