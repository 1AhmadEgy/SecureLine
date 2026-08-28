package com.secureline.server;

import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityManager {

    private final Map<String, Long> blockedIps;
    private final Map<String, Integer> failedAttempts;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long BLOCK_DURATION = 15 * 60 * 1000;

    public SecurityManager() {
        blockedIps = new ConcurrentHashMap<>();
        failedAttempts = new ConcurrentHashMap<>();
    }

    public void recordFailedAttempt(String ipAddress) {
        int attempts = failedAttempts.getOrDefault(ipAddress, 0) + 1;
        failedAttempts.put(ipAddress, attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            blockedIps.put(ipAddress, System.currentTimeMillis() + BLOCK_DURATION);
            failedAttempts.remove(ipAddress);
        }
    }

    public void recordSuccessfulAttempt(String ipAddress) {
        failedAttempts.remove(ipAddress);
    }

    public boolean isBlocked(String ipAddress) {
        Long blockUntil = blockedIps.get(ipAddress);
        if (blockUntil == null) return false;

        if (System.currentTimeMillis() > blockUntil) {
            blockedIps.remove(ipAddress);
            return false;
        }
        return true;
    }

    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String combined = salt + password;
            byte[] hash = digest.digest(combined.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
