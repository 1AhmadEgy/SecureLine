package com.secureline.server;

import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationHandler {

    private final Map<String, String> userCredentials;
    private final Map<String, String> activeSessions;
    private final Map<String, Long> sessionExpiries;

    public AuthenticationHandler() {
        userCredentials = new ConcurrentHashMap<>();
        activeSessions = new ConcurrentHashMap<>();
        sessionExpiries = new ConcurrentHashMap<>();
    }

    public boolean registerUser(String username, String passwordHash) {
        if (userCredentials.containsKey(username)) return false;
        userCredentials.put(username, passwordHash);
        return true;
    }

    public boolean authenticate(String username, String passwordHash) {
        String storedHash = userCredentials.get(username);
        return storedHash != null && storedHash.equals(passwordHash);
    }

    public String createSession(String username) {
        String sessionToken = generateToken();
        activeSessions.put(sessionToken, username);
        sessionExpiries.put(sessionToken, System.currentTimeMillis() + (30 * 60 * 1000));
        return sessionToken;
    }

    public boolean isValidSession(String sessionToken) {
        String username = activeSessions.get(sessionToken);
        if (username == null) return false;

        Long expiry = sessionExpiries.get(sessionToken);
        if (expiry == null || System.currentTimeMillis() > expiry) {
            activeSessions.remove(sessionToken);
            sessionExpiries.remove(sessionToken);
            return false;
        }
        return true;
    }

    public String getUsernameFromSession(String sessionToken) {
        return activeSessions.get(sessionToken);
    }

    public void invalidateSession(String sessionToken) {
        activeSessions.remove(sessionToken);
        sessionExpiries.remove(sessionToken);
    }

    private String generateToken() {
        byte[] random = new byte[32];
        new java.security.SecureRandom().nextBytes(random);
        StringBuilder sb = new StringBuilder();
        for (byte b : random) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
