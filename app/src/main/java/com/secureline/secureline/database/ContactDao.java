package com.secureline.secureline.database;

import android.content.ContentValues;
import android.database.Cursor;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContactDao {

    private final SQLiteDatabase db;

    public ContactDao(SQLiteDatabase database) {
        this.db = database;
    }

    public long insertContact(String displayName, String publicKey) {
        ContentValues values = new ContentValues();
        values.put("contact_uuid", UUID.randomUUID().toString());
        values.put("display_name", displayName);
        values.put("public_key", publicKey);
        values.put("is_verified", 0);
        values.put("is_blocked", 0);
        values.put("last_seen", 0);
        return db.insert("contacts", null, values);
    }

    public List<String> getAllContacts() {
        List<String> contacts = new ArrayList<>();
        Cursor cursor = db.query(
            "contacts",
            new String[]{"display_name", "contact_uuid", "is_verified"},
            "is_blocked = 0",
            null, null, null,
            "display_name ASC"
        );

        while (cursor != null && cursor.moveToNext()) {
            String name = cursor.getString(0);
            String uuid = cursor.getString(1);
            int verified = cursor.getInt(2);
            contacts.add(name);
        }
        if (cursor != null) cursor.close();
        return contacts;
    }

    public List<String> getAllContactIds() {
        List<String> contactIds = new ArrayList<>();
        Cursor cursor = db.query(
            "contacts",
            new String[]{"contact_uuid"},
            null, null, null, null, null
        );
        while (cursor != null && cursor.moveToNext()) {
            contactIds.add(cursor.getString(0));
        }
        if (cursor != null) cursor.close();
        return contactIds;
    }

    public String getContactIdByName(String displayName) {
        Cursor cursor = db.query(
            "contacts",
            new String[]{"contact_uuid"},
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

    public void verifyContact(String contactUuid) {
        ContentValues values = new ContentValues();
        values.put("is_verified", 1);
        db.update("contacts", values, "contact_uuid = ?", new String[]{contactUuid});
    }

    public void unverifyContact(String contactUuid) {
        ContentValues values = new ContentValues();
        values.put("is_verified", 0);
        db.update("contacts", values, "contact_uuid = ?", new String[]{contactUuid});
    }

    public void blockContact(String contactUuid) {
        ContentValues values = new ContentValues();
        values.put("is_blocked", 1);
        db.update("contacts", values, "contact_uuid = ?", new String[]{contactUuid});
    }

    public void unblockContact(String contactUuid) {
        ContentValues values = new ContentValues();
        values.put("is_blocked", 0);
        db.update("contacts", values, "contact_uuid = ?", new String[]{contactUuid});
    }

    public void deleteContact(String contactUuid) {
        db.delete("contacts", "contact_uuid = ?", new String[]{contactUuid});
    }

    public void updateLastSeen(String contactUuid, long lastSeen) {
        ContentValues values = new ContentValues();
        values.put("last_seen", lastSeen);
        db.update("contacts", values, "contact_uuid = ?", new String[]{contactUuid});
    }

    public boolean isContactVerified(String contactUuid) {
        Cursor cursor = db.query(
            "contacts",
            new String[]{"is_verified"},
            "contact_uuid = ?",
            new String[]{contactUuid},
            null, null, null
        );
        boolean verified = false;
        if (cursor != null && cursor.moveToFirst()) {
            verified = cursor.getInt(0) == 1;
        }
        if (cursor != null) cursor.close();
        return verified;
    }
}
