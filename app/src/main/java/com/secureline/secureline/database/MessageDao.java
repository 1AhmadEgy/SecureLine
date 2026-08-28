package com.secureline.secureline.database;

import android.content.ContentValues;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MessageDao {

    private final SQLiteDatabase db;

    public MessageDao(SQLiteDatabase database) {
        this.db = database;
    }

    public long insertMessage(String uuid, String conversationUuid, 
                               String senderUuid, String body, long timestamp) {
        ContentValues values = new ContentValues();
        values.put("message_uuid", uuid);
        values.put("conversation_uuid", conversationUuid);
        values.put("sender_uuid", senderUuid);
        values.put("body", body);
        values.put("timestamp", timestamp);
        return db.insert("messages", null, values);
    }

    public List<String> getMessagesForConversation(String conversationUuid) {
        List<String> messages = new ArrayList<>();
        android.database.Cursor cursor = db.query(
            "messages",
            new String[]{"body", "sender_uuid", "timestamp"},
            "conversation_uuid = ?",
            new String[]{conversationUuid},
            null, null,
            "timestamp ASC"
        );

        while (cursor.moveToNext()) {
            String body = cursor.getString(0);
            String sender = cursor.getString(1);
            long timestamp = cursor.getLong(2);
            messages.add("[" + timestamp + "] " + sender + ": " + body);
        }
        cursor.close();
        return messages;
    }

    public void markAsRead(String messageUuid) {
        ContentValues values = new ContentValues();
        values.put("is_read", 1);
        db.update("messages", values, "message_uuid = ?", new String[]{messageUuid});
    }

    public void deleteMessage(String messageUuid) {
        db.delete("messages", "message_uuid = ?", new String[]{messageUuid});
    }

    public void deleteAllMessages() {
        db.delete("messages", null, null);
    }
}
