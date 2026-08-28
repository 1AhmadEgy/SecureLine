package com.secureline.secureline.database;

import android.content.ContentValues;
import android.database.Cursor;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageDao {

    private final SQLiteDatabase db;

    public MessageDao(SQLiteDatabase database) {
        this.db = database;
    }

    public long insertMessage(String conversationUuid, String senderUuid, String body) {
        ContentValues values = new ContentValues();
        values.put("message_uuid", UUID.randomUUID().toString());
        values.put("conversation_uuid", conversationUuid);
        values.put("sender_uuid", senderUuid);
        values.put("body", body);
        values.put("timestamp", System.currentTimeMillis());
        values.put("is_read", 0);
        values.put("is_deleted", 0);
        return db.insert("messages", null, values);
    }

    public List<String> getMessagesForConversation(String conversationUuid) {
        List<String> messages = new ArrayList<>();
        Cursor cursor = db.query(
            "messages",
            new String[]{"sender_uuid", "body", "timestamp"},
            "conversation_uuid = ? AND is_deleted = 0",
            new String[]{conversationUuid},
            null, null,
            "timestamp ASC"
        );

        while (cursor != null && cursor.moveToNext()) {
            String sender = cursor.getString(0);
            String body = cursor.getString(1);
            long timestamp = cursor.getLong(2);
            if (sender.equals("me")) {
                messages.add("[" + timestamp + "] أنا: " + body);
            } else {
                messages.add("[" + timestamp + "] " + sender + ": " + body);
            }
        }
        if (cursor != null) cursor.close();
        return messages;
    }

    public void markAsRead(String messageUuid) {
        ContentValues values = new ContentValues();
        values.put("is_read", 1);
        db.update("messages", values, "message_uuid = ?", new String[]{messageUuid});
    }

    public void markAllAsRead(String conversationUuid) {
        ContentValues values = new ContentValues();
        values.put("is_read", 1);
        db.update("messages", values, "conversation_uuid = ?", new String[]{conversationUuid});
    }

    public void deleteMessage(String messageUuid) {
        ContentValues values = new ContentValues();
        values.put("is_deleted", 1);
        db.update("messages", values, "message_uuid = ?", new String[]{messageUuid});
    }

    public void deleteAllMessages(String conversationUuid) {
        ContentValues values = new ContentValues();
        values.put("is_deleted", 1);
        db.update("messages", values, "conversation_uuid = ?", new String[]{conversationUuid});
    }

    public int getUnreadCount(String conversationUuid) {
        Cursor cursor = db.query(
            "messages",
            new String[]{"COUNT(*)"},
            "conversation_uuid = ? AND is_read = 0 AND is_deleted = 0",
            new String[]{conversationUuid},
            null, null, null
        );
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        if (cursor != null) cursor.close();
        return count;
    }

    public String getLastMessage(String conversationUuid) {
        Cursor cursor = db.query(
            "messages",
            new String[]{"body", "timestamp"},
            "conversation_uuid = ? AND is_deleted = 0",
            new String[]{conversationUuid},
            null, null,
            "timestamp DESC",
            "1"
        );
        String lastMessage = "";
        if (cursor != null && cursor.moveToFirst()) {
            lastMessage = cursor.getString(0);
        }
        if (cursor != null) cursor.close();
        return lastMessage;
    }
}
