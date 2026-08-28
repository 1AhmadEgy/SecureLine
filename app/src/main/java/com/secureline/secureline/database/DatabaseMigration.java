package com.secureline.secureline.database;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

public class DatabaseMigration {

    public static void migrateV1toV2(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE messages ADD COLUMN is_deleted INTEGER DEFAULT 0");
        db.execSQL("ALTER TABLE messages ADD COLUMN delivered_at INTEGER DEFAULT 0");
        db.execSQL("ALTER TABLE messages ADD COLUMN read_at INTEGER DEFAULT 0");
    }

    public static void migrateV2toV3(SQLiteDatabase db) {
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_messages_conversation ON messages(conversation_uuid)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_messages_timestamp ON messages(timestamp)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_contacts_uuid ON contacts(contact_uuid)");
    }

    public static void migrateV3toV4(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE contacts ADD COLUMN last_seen INTEGER DEFAULT 0");
        db.execSQL("ALTER TABLE contacts ADD COLUMN is_blocked INTEGER DEFAULT 0");
    }
}