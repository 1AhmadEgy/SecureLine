package com.secureline.secureline.util;

public class ErrorCodes {

    public static final int SUCCESS = 0;
    public static final int ERROR_UNKNOWN = 1;
    public static final int ERROR_NETWORK = 2;
    public static final int ERROR_ENCRYPTION = 3;
    public static final int ERROR_DECRYPTION = 4;
    public static final int ERROR_AUTHENTICATION = 5;
    public static final int ERROR_KEY_EXCHANGE = 6;
    public static final int ERROR_DATABASE = 7;
    public static final int ERROR_PERMISSION = 8;
    public static final int ERROR_INVALID_INPUT = 9;
    public static final int ERROR_TIMEOUT = 10;
    public static final int ERROR_SERVER = 11;
    public static final int ERROR_TOR = 12;
    public static final int ERROR_CALL = 13;
    public static final int ERROR_MEDIA = 14;
    public static final int ERROR_STORAGE = 15;

    public static String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case SUCCESS: return "Success";
            case ERROR_UNKNOWN: return "Unknown error";
            case ERROR_NETWORK: return "Network error";
            case ERROR_ENCRYPTION: return "Encryption failed";
            case ERROR_DECRYPTION: return "Decryption failed";
            case ERROR_AUTHENTICATION: return "Authentication failed";
            case ERROR_KEY_EXCHANGE: return "Key exchange failed";
            case ERROR_DATABASE: return "Database error";
            case ERROR_PERMISSION: return "Permission denied";
            case ERROR_INVALID_INPUT: return "Invalid input";
            case ERROR_TIMEOUT: return "Operation timed out";
            case ERROR_SERVER: return "Server error";
            case ERROR_TOR: return "Tor connection failed";
            case ERROR_CALL: return "Call failed";
            case ERROR_MEDIA: return "Media error";
            case ERROR_STORAGE: return "Storage error";
            default: return "Unknown error";
        }
    }
}
