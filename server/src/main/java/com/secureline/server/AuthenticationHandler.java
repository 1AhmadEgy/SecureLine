package com.secureline.server;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationHandler {

    private static final int SALT_BYTES = 16;
    private static final int HASH_ITERATIONS = 120_000;
    private static final int HASH_BYTES = 32;
    private static final long SESSION_TTL_MS = 30 * 60 * 1000L;

    private final Map<String, Credential> userCredentials = new ConcurrentHashMap<>();
    private final Map<String, String> activeSessions = new ConcurrentHashMap<>();
    private final Map<String, Long> sessionExpiries = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    public boolean registerUser(String username, String password) {
        if (username == null || password == null || username.isBlank() || password.length() < 8) {
            return false;
        }
        byte[] salt = new byte[SALT_BYTES];
        secureRandom.nextBytes(salt);
        byte[] hash = derive(password.toCharArray(), salt);
        return userCredentials.putIfAbsent(username, new Credential(salt, hash)) == null;
    }

    /**
     * Legacy compatibility: callers that already provide a derived credential
     * are rejected here rather than treating an arbitrary client hash as a password.
     */
    public boolean registerUserHash(String username, String passwordHash) {
        return false;
    }

    public boolean authenticate(String username, String password) {
        Credential credential = userCredentials.get(username);
        if (credential == null || password == null) return false;

        byte[] computed = derive(password.toCharArray(), credential.salt);
        boolean valid = MessageDigest.isEqual(credential.hash, computed);
        if (!valid) return false;

        return true;
    }

    public String createSession(String username) {
        if (username == null || !userCredentials.containsKey(username)) return null;
        byte[] random = new byte[32];
        secureRandom.nextBytes(random);
        String sessionToken = Base64.getUrlEncoder().withoutPadding().encodeToString(random);
        activeSessions.put(sessionToken, username);
        sessionExpiries.put(sessionToken, System.currentTimeMillis() + SESSION_TTL_MS);
        return sessionToken;
    }

    public boolean isValidSession(String sessionToken) {
        String username = activeSessions.get(sessionToken);
        if (username == null) return false;
        Long expiry = sessionExpiries.get(sessionToken);
        if (expiry == null || System.currentTimeMillis() >= expiry) {
            invalidateSession(sessionToken);
            return false;
        }
        return true;
    }

    public String getUsernameFromSession(String sessionToken) {
        return isValidSession(sessionToken) ? activeSessions.get(sessionToken) : null;
    }

    public void invalidateSession(String sessionToken) {
        if (sessionToken == null) return;
        activeSessions.remove(sessionToken);
        sessionExpiries.remove(sessionToken);
    }

    private byte[] derive(char[] password, byte[] salt) {
        try {
            javax.crypto.SecretKeyFactory factory =
                    javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            javax.crypto.spec.PBEKeySpec spec =
                    new javax.crypto.spec.PBEKeySpec(password, salt, HASH_ITERATIONS, HASH_BYTES * 8);
            return factory.generateSecret(spec).getEncoded();
        } catch (java.security.GeneralSecurityException e) {
            throw new IllegalStateException("Unable to derive password key", e);
        } finally {
            java.util.Arrays.fill(password, '\\0');
        }
    }

    private static final class Credential {
        private final byte[] salt;
        private final byte[] hash;

        private Credential(byte[] salt, byte[] hash) {
            this.salt = salt.clone();
            this.hash = hash.clone();
        }
    }
}
