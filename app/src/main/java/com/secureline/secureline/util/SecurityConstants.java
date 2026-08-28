package com.secureline.secureline.util;

public class SecurityConstants {

    public static final int AES_KEY_SIZE = 256;
    public static final int AES_IV_SIZE = 12;
    public static final int AES_TAG_SIZE = 128;

    public static final int CHACHA_KEY_SIZE = 32;
    public static final int CHACHA_NONCE_SIZE = 12;
    public static final int CHACHA_TAG_SIZE = 16;

    public static final int SALT_SIZE = 16;
    public static final int HASH_SIZE = 32;

    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_MESSAGE_LENGTH = 10000;

    public static final long SESSION_TIMEOUT = 30 * 60 * 1000;
    public static final long KEY_ROTATION_INTERVAL = 24 * 60 * 60 * 1000;

    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static final long RETRY_BASE_DELAY = 1000;
    public static final long RETRY_MAX_DELAY = 60000;

    public static final String KEY_ALIAS_DATABASE = "secureline_db_key";
    public static final String KEY_ALIAS_MESSAGES = "secureline_msg_key";
    public static final String KEY_ALIAS_CALLS = "secureline_call_key";
}
