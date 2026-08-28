package com.secureline.secureline.security;

import com.secureline.secureline.crypto.HashUtils;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {

    private final Map<String, byte[]> userCredentials;
    private final Map<String, Long> lastAuthTimes;

    public AuthenticationService() {
        userCredentials = new HashMap<>();
        lastAuthTimes = new HashMap<>();
    }

    public void registerUser(String username, String password) {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        byte[] passwordHash = HashUtils.sha256(
            java.util.Arrays.copyOf(salt, salt.length + password.length())
        );
        userCredentials.put(username, passwordHash);
    }

    public boolean authenticateUser(String username, String password) {
        byte[] storedHash = userCredentials.get(username);
        if (storedHash == null) return false;

        byte[] computedHash = HashUtils.sha256(password.getBytes());
        boolean authenticated = java.security.MessageDigest.isEqual(storedHash, computedHash);

        if (authenticated) {
            lastAuthTimes.put(username, System.currentTimeMillis());
        }
        return authenticated;
    }

    public long getLastAuthTime(String username) {
        Long time = lastAuthTimes.get(username);
        return time != null ? time : -1;
    }

    public void logoutUser(String username) {
        lastAuthTimes.remove(username);
    }
}
