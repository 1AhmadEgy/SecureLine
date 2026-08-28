package com.secureline.secureline.database;

import android.content.ContentValues;
import android.database.Cursor;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConversationDao {

    private final SQLiteDatabase db;

    public ConversationDao(SQLiteDatabase database) {
        this.db = database;
    }

    public long createConversation(String displayName) {
        ContentValues values = new ContentValues();
        values.put("conversation_uuid", UUID.randomUUID().toString());
        values.put("display_name", displayName);
        values.put("created_at", System.currentTimeMillis());
        values.put("last_message_time", 0);
        return db.insert("conversations", null, values);
    }

    public List<String> getAllConversations() {
        List<String> conversations = new ArrayList<>();
        Cursor cursor = db.query(
            "conversations",
            new String[]{"display_name", "conversation_uuid", "last_message_time"},
            null, null, null, null,
            "last_message_time DESC"
        );
        while (cursor != null && cursor.moveToNext()) {
            conversations.add(cursor.getString(0));
        }
        if (cursor != null) cursor.close();
        return conversations;
    }

    public String getConversationIdByName(String displayName) {
        Cursor cursor = db.query(
            "conversations",
            new String[]{"conversation_uuid"},
            "display_name = ?",
            new String[]{displayName},
            null, null, null
        );
        String id = null;
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(0);
        }
        if (cursor != null) cursor.close();
        return id;
    }

    public void updateLastMessageTime(String conversationUuid) {
        ContentValues values = new ContentValues();
        values.put("last_message_time", System.currentTimeMillis());
        db.update("conversations", values, "conversation_uuid = ?", 
            new String[]{conversationUuid});
    }

    public void deleteConversation(String conversationUuid) {
        db.delete("conversations", "conversation_uuid = ?", new String[]{conversationUuid});
        db.delete("messages", "conversation_uuid = ?", new String[]{conversationUuid});
    }
}
