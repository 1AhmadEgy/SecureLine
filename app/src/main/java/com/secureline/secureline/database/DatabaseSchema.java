package com.secureline.secureline.database;

public class DatabaseSchema {

    public static final String CREATE_TABLE_SESSIONS =
        "CREATE TABLE IF NOT EXISTS sessions (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "session_id TEXT UNIQUE NOT NULL, " +
        "created_at INTEGER NOT NULL)";

    public static final String CREATE_TABLE_MESSAGES =
        "CREATE TABLE IF NOT EXISTS messages (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "message_uuid TEXT UNIQUE NOT NULL, " +
        "conversation_uuid TEXT NOT NULL, " +
        "sender_uuid TEXT NOT NULL, " +
        "body TEXT NOT NULL, " +
        "timestamp INTEGER NOT NULL, " +
        "is_read INTEGER DEFAULT 0, " +
        "is_deleted INTEGER DEFAULT 0)";

    public static final String CREATE_TABLE_CONTACTS =
        "CREATE TABLE IF NOT EXISTS contacts (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "contact_uuid TEXT UNIQUE NOT NULL, " +
        "display_name TEXT NOT NULL, " +
        "public_key TEXT NOT NULL, " +
        "is_verified INTEGER DEFAULT 0, " +
        "is_blocked INTEGER DEFAULT 0)";

    public static final String CREATE_TABLE_KEYS =
        "CREATE TABLE IF NOT EXISTS keys (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "key_alias TEXT UNIQUE NOT NULL, " +
        "key_data TEXT NOT NULL, " +
        "created_at INTEGER NOT NULL)";

    public static final String CREATE_TABLE_CONVERSATIONS =
        "CREATE TABLE IF NOT EXISTS conversations (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "conversation_uuid TEXT UNIQUE NOT NULL, " +
        "display_name TEXT NOT NULL, " +
        "created_at INTEGER NOT NULL, " +
        "last_message_time INTEGER DEFAULT 0)";

    public static final String CREATE_TABLE_GROUPS =
        "CREATE TABLE IF NOT EXISTS groups (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "group_uuid TEXT UNIQUE NOT NULL, " +
        "group_name TEXT NOT NULL, " +
        "admin_uuid TEXT NOT NULL)";

    public static final String CREATE_TABLE_GROUP_MEMBERS =
        "CREATE TABLE IF NOT EXISTS group_members (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "group_uuid TEXT NOT NULL, " +
        "member_uuid TEXT NOT NULL)";
}
