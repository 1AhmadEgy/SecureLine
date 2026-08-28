package com.secureline.secureline.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.security.SecureRandom;

public class SessionManager {

    private static final String PREFS_NAME = "secureline_sessions";
    private static final String SESSION_KEY = "session_token";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String createSessionToken() {
        byte[] tokenBytes = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(tokenBytes);
        String token = Base64.encodeToString(tokenBytes, Base64.NO_WRAP);
        prefs.edit().putString(SESSION_KEY, token).apply();
        return token;
    }

    public String getSessionToken() {
        return prefs.getString(SESSION_KEY, null);
    }

    public boolean isValidSession() {
        return getSessionToken() != null;
    }

    public void clearSession() {
        prefs.edit().remove(SESSION_KEY).apply();
    }
}
