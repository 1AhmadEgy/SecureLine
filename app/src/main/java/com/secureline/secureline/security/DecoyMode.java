package com.secureline.secureline.security;

import android.content.Context;
import android.content.SharedPreferences;

public class DecoyMode {

    private static final String PREFS_NAME = "secureline_decoy";
    private static final String DECOY_KEY = "decoy_enabled";

    private final SharedPreferences prefs;

    public DecoyMode(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void enableDecoyMode() {
        prefs.edit().putBoolean(DECOY_KEY, true).apply();
    }

    public void disableDecoyMode() {
        prefs.edit().putBoolean(DECOY_KEY, false).apply();
    }

    public boolean isDecoyModeEnabled() {
        return prefs.getBoolean(DECOY_KEY, false);
    }

    public void setDecoyPassword(String password) {
        String hashed = HashUtils.sha256Hex(password.getBytes());
        prefs.edit().putString("decoy_password_hash", hashed).apply();
    }

    public boolean checkDecoyPassword(String password) {
        String storedHash = prefs.getString("decoy_password_hash", null);
        if (storedHash == null) return false;
        String inputHash = HashUtils.sha256Hex(password.getBytes());
        return storedHash.equals(inputHash);
    }
}
