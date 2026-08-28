package com.secureline.secureline.util;

public class TextUtils {

    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }

    public static String sanitize(String text) {
        if (text == null) return "";
        return text.replace("<", "&lt;").replace(">", "&gt;");
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static String joinStrings(String separator, String... parts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append(separator);
            sb.append(parts[i]);
        }
        return sb.toString();
    }
}
