package com.secureline.secureline.security;

import android.content.Context;
import android.content.SharedPreferences;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import android.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class AuthenticationService {

    private static final String PREFS = "secureline_auth";
    private static final String USERS = "users";
    private static final int SALT_BYTES = 16;
    private static final int ITERATIONS = 120_000;
    private static final int KEY_BITS = 256;

    private final SharedPreferences prefs;

    public AuthenticationService(Context context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public boolean registerUser(String username, String password) {
        String normalized = normalize(username);
        validatePassword(password);
        if (prefs.contains(userKey(normalized))) {
            return false;
        }

        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] hash = derive(password, salt);

        prefs.edit()
            .putString(userKey(normalized), Base64.encodeToString(salt, Base64.NO_WRAP))
            .putString(hashKey(normalized), Base64.encodeToString(hash, Base64.NO_WRAP))
            .apply();
        return true;
    }

    public boolean authenticateUser(String username, String password) {
        String normalized = normalize(username);
        String saltEncoded = prefs.getString(userKey(normalized), null);
        String hashEncoded = prefs.getString(hashKey(normalized), null);
        if (saltEncoded == null || hashEncoded == null) {
            return false;
        }

        byte[] salt = Base64.decode(saltEncoded, Base64.DEFAULT);
        byte[] expected = Base64.decode(hashEncoded, Base64.DEFAULT);
        byte[] actual = derive(password, salt);
        boolean authenticated = MessageDigest.isEqual(expected, actual);
        if (authenticated) {
            prefs.edit().putLong(lastAuthKey(normalized), System.currentTimeMillis()).apply();
        }
        return authenticated;
    }

    public long getLastAuthTime(String username) {
        return prefs.getLong(lastAuthKey(normalize(username)), -1L);
    }

    public void logoutUser(String username) {
        prefs.edit().remove(lastAuthKey(normalize(username))).apply();
    }

    private static byte[] derive(String password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_BITS);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to derive password verifier", e);
        }
    }

    private static String normalize(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username is required");
        }
        String value = username.trim();
        if (value.length() < 3 || value.length() > 64) {
            throw new IllegalArgumentException("Username must be 3-64 characters");
        }
        return value.toLowerCase(java.util.Locale.ROOT);
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() < 8 || password.length() > 256) {
            throw new IllegalArgumentException("Password must be 8-256 characters");
        }
    }

    private static String userKey(String username) {
        return USERS + "_salt_" + username;
    }

    private static String hashKey(String username) {
        return USERS + "_hash_" + username;
    }

    private static String lastAuthKey(String username) {
        return USERS + "_last_auth_" + username;
    }
}
