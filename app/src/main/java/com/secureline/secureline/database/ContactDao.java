package com.secureline.secureline.database;

import android.content.ContentValues;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContactDao {

    private final SQLiteDatabase db;

    public ContactDao(SQLiteDatabase database) {
        this.db = database;
    }

    public long insertContact(String uuid, String displayName, String publicKey) {
        ContentValues values = new ContentValues();
        values.put("contact_uuid", uuid);
        values.put("display_name", displayName);
        values.put("public_key", publicKey);
        return db.insert("contacts", null, values);
    }

    public List<String> getAllContacts() {
        List<String> contacts = new ArrayList<>();
        android.database.Cursor cursor = db.query(
            "contacts",
            new String[]{"display_name", "contact_uuid", "is_verified"},
            null, null, null, null,
            "display_name ASC"
        );

        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String uuid = cursor.getString(1);
            int verified = cursor.getInt(2);
            contacts.add(name + " (" + uuid + ") " + (verified == 1 ? "[✓]" : ""));
        }
        cursor.close();
        return contacts;
    }

    public void verifyContact(String contactUuid) {
        ContentValues values = new ContentValues();
        values.put("is_verified", 1);
        db.update("contacts", values, "contact_uuid = ?", new String[]{contactUuid});
    }

    public void deleteContact(String contactUuid) {
        db.delete("contacts", "contact_uuid = ?", new String[]{contactUuid});
    }
}
