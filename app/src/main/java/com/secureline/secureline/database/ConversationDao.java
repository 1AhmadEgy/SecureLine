package com.secureline.secureline.database;

import android.content.ContentValues;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ConversationDao {

    private final SQLiteDatabase db;

    public ConversationDao(SQLiteDatabase database) {
        this.db = database;
    }

    public long insertConversation(String uuid, String displayName, long createdAt) {
        ContentValues values = new ContentValues();
        values.put("conversation_uuid", uuid);
        values.put("display_name", displayName);
        values.put("created_at", createdAt);
        return db.insert("conversations", null, values);
    }

    public List<String> getAllConversations() {
        List<String> conversations = new ArrayList<>();
        android.database.Cursor cursor = db.query(
            "conversations",
            new String[]{"conversation_uuid", "display_name", "last_message_time"},
            null, null, null, null,
            "last_message_time DESC"
        );

        while (cursor.moveToNext()) {
            String uuid = cursor.getString(0);
            String name = cursor.getString(1);
            long lastMessage = cursor.getLong(2);
            conversations.add(name + " (" + uuid + ") - " + lastMessage);
        }
        cursor.close();
        return conversations;
    }

    public void updateLastMessageTime(String conversationUuid, long timestamp) {
        ContentValues values = new ContentValues();
        values.put("last_message_time", timestamp);
        db.update("conversations", values, "conversation_uuid = ?", 
            new String[]{conversationUuid});
    }

    public void deleteConversation(String conversationUuid) {
        db.delete("conversations", "conversation_uuid = ?", 
            new String[]{conversationUuid});
    }
}
