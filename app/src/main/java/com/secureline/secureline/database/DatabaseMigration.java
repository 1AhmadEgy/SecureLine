package com.secureline.secureline.database;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

public final class DatabaseMigration {

    private DatabaseMigration() {}

    public static void migrate(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;

        if (version < 2 && newVersion >= 2) {
            migrateV1toV2(db);
            version = 2;
        }
        if (version < 3 && newVersion >= 3) {
            migrateV2toV3(db);
            version = 3;
        }
        if (version < 4 && newVersion >= 4) {
            migrateV3toV4(db);
        }
    }

    private static void migrateV1toV2(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE messages ADD COLUMN is_deleted INTEGER DEFAULT 0");
        db.execSQL("ALTER TABLE messages ADD COLUMN delivered_at INTEGER DEFAULT 0");
        db.execSQL("ALTER TABLE messages ADD COLUMN read_at INTEGER DEFAULT 0");
    }

    private static void migrateV2toV3(SQLiteDatabase db) {
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_messages_conversation ON messages(conversation_uuid)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_messages_timestamp ON messages(timestamp)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_contacts_uuid ON contacts(contact_uuid)");
    }

    private static void migrateV3toV4(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE contacts ADD COLUMN last_seen INTEGER DEFAULT 0");
        db.execSQL("ALTER TABLE contacts ADD COLUMN is_blocked INTEGER DEFAULT 0");
    }
}
