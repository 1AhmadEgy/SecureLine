package com.secureline.secureline.util;

public class Validator {

    public static boolean isValidUuid(String uuid) {
        return uuid != null && uuid.matches(
            "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
        );
    }

    public static boolean isValidPublicKey(String base64Key) {
        if (base64Key == null || base64Key.isEmpty()) return false;
        try {
            byte[] decoded = android.util.Base64.decode(base64Key, android.util.Base64.NO_WRAP);
            return decoded.length >= 32;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidMessage(String message) {
        return message != null && !message.trim().isEmpty() && message.length() <= 10000;
    }

    public static boolean isValidDisplayName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 50;
    }
}
