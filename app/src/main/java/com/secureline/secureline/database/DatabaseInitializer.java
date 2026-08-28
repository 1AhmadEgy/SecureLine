package com.secureline.secureline.database;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

public class DatabaseInitializer {

    public static void initialize(SQLiteDatabase db) {
        db.execSQL(DatabaseSchema.CREATE_TABLE_SESSIONS);
        db.execSQL(DatabaseSchema.CREATE_TABLE_MESSAGES);
        db.execSQL(DatabaseSchema.CREATE_TABLE_CONTACTS);
        db.execSQL(DatabaseSchema.CREATE_TABLE_KEYS);
        db.execSQL(DatabaseSchema.CREATE_TABLE_CONVERSATIONS);
        db.execSQL(DatabaseSchema.CREATE_TABLE_GROUPS);
        db.execSQL(DatabaseSchema.CREATE_TABLE_GROUP_MEMBERS);

        createIndexes(db);
    }

    private static void createIndexes(SQLiteDatabase db) {
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_messages_conversation ON messages(conversation_uuid)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_messages_timestamp ON messages(timestamp)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_messages_sender ON messages(sender_uuid)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_contacts_uuid ON contacts(contact_uuid)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_contacts_name ON contacts(display_name)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_keys_alias ON keys(key_alias)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_conversations_uuid ON conversations(conversation_uuid)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_groups_uuid ON groups(group_uuid)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_group_members_group ON group_members(group_uuid)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_group_members_member ON group_members(member_uuid)");
    }
}
