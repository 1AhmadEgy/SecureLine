package com.secureline.secureline.util;

public class ValidationUtils {

    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) return false;
        return username.matches("^[a-zA-Z0-9_]{3,30}$");
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        return hasLetter && hasDigit;
    }

    public static boolean isValidMessage(String message) {
        return message != null && !message.trim().isEmpty() && message.length() <= 10000;
    }

    public static boolean isValidContactName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 50;
    }

    public static boolean isValidFingerprint(String fingerprint) {
        if (fingerprint == null) return false;
        return fingerprint.replace(" ", "").matches("^[0-9a-fA-F]{40}$");
    }

    public static boolean isValidBase64(String base64) {
        if (base64 == null) return false;
        try {
            android.util.Base64.decode(base64, android.util.Base64.NO_WRAP);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
