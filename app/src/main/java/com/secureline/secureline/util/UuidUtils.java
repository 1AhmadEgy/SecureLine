package com.secureline.secureline.util;

import java.util.UUID;

public class UuidUtils {

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public static String generateUuidWithoutDashes() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static boolean isValidUuid(String uuid) {
        if (uuid == null) return false;
        try {
            UUID.fromString(uuid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
