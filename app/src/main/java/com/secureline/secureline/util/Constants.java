package com.secureline.secureline.util;

public class Constants {

    public static final String APP_NAME = "SecureLine";
    public static final String APP_VERSION = "1.0.0";

    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_MESSAGE_LENGTH = 10000;
    public static final int MAX_CONTACT_NAME_LENGTH = 50;

    public static final long SESSION_TIMEOUT_MILLIS = 24 * 60 * 60 * 1000;
    public static final long KEY_ROTATION_INTERVAL = 24 * 60 * 60 * 1000;
    public static final long MESSAGE_EXPIRY_DEFAULT = 0;
    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static final long RETRY_BASE_DELAY = 1000;
    public static final long RETRY_MAX_DELAY = 60000;

    public static final int AES_KEY_SIZE = 256;
    public static final int IV_SIZE = 12;
    public static final int TAG_SIZE = 128;
    public static final int SALT_SIZE = 16;
    public static final int HASH_SIZE = 32;

    public static final String KEY_ALIAS = "secureline_master_key";
    public static final String PREF_NAME = "secureline_prefs";
    public static final String DATABASE_NAME = "secureline.db";
}
