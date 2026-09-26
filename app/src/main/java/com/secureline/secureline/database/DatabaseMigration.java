package com.secureline.secureline.database;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

public final class DatabaseMigration {

    private DatabaseMigration() {}

    public static void migrate(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;

        if (version < 2 && newVersion >= 2) {
            addColumnIfMissing(db, "messages", "is_deleted", "INTEGER DEFAULT 0");
            addColumnIfMissing(db, "messages", "delivered_at", "INTEGER DEFAULT 0");
            addColumnIfMissing(db, "messages", "read_at", "INTEGER DEFAULT 0");
            version = 2;
        }

        if (version < 3 && newVersion >= 3) {
            createIndexes(db);
            version = 3;
        }

        if (version < 4 && newVersion >= 4) {
            addColumnIfMissing(db, "contacts", "last_seen", "INTEGER DEFAULT 0");
            addColumnIfMissing(db, "contacts", "is_blocked", "INTEGER DEFAULT 0");
            version = 4;
        }

        if (version < 5 && newVersion >= 5) {
            migrateV4toV5(db);
            version = 5;
        }

        if (version < newVersion) {
            createIndexes(db);
        }
    }

    private static void addColumnIfMissing(
            SQLiteDatabase db, String table, String column, String definition) {
        if (!hasColumn(db, table, column)) {
            db.execSQL("ALTER TABLE " + table + " ADD COLUMN " + column + " " + definition);
        }
    }

    private static boolean hasColumn(SQLiteDatabase db, String table, String column) {
        try (android.database.Cursor cursor =
                     db.rawQuery("PRAGMA table_info(" + table + ")", null)) {
            int nameIndex = cursor.getColumnIndex("name");
            if (nameIndex < 0) {
                return false;
            }
            while (cursor.moveToNext()) {
                if (column.equalsIgnoreCase(cursor.getString(nameIndex))) {
                    return true;
                }
            }
            return false;
        }
    }

    private static void migrateV4toV5(SQLiteDatabase db) {
        db.execSQL(DatabaseSchema.CREATE_TABLE_SIGNAL_IDENTITY);
        db.execSQL(DatabaseSchema.CREATE_TABLE_SIGNAL_PRE_KEYS);
        db.execSQL(DatabaseSchema.CREATE_TABLE_SIGNAL_SIGNED_PRE_KEY);
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_signal_pre_keys_id ON signal_pre_keys(pre_key_id)");
    }

    private static void createIndexes(SQLiteDatabase db) {
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_messages_conversation ON messages(conversation_uuid)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_messages_timestamp ON messages(timestamp)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_contacts_uuid ON contacts(contact_uuid)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_contacts_name ON contacts(display_name)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_keys_alias ON keys(key_alias)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_conversations_uuid ON conversations(conversation_uuid)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_groups_uuid ON groups(group_uuid)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_group_members_group ON group_members(group_uuid)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_group_members_member ON group_members(member_uuid)");
    }
}
