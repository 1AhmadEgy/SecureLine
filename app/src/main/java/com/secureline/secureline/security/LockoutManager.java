package com.secureline.secureline.security;

import android.content.Context;
import android.content.SharedPreferences;

public class LockoutManager {

    private static final String PREFS_NAME = "secureline_lockout";
    private static final String FAILED_ATTEMPTS = "failed_attempts";
    private static final String LOCKOUT_UNTIL = "lockout_until";
    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION = 300000;

    private final SharedPreferences prefs;

    public LockoutManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void recordFailedAttempt() {
        int attempts = prefs.getInt(FAILED_ATTEMPTS, 0) + 1;
        prefs.edit().putInt(FAILED_ATTEMPTS, attempts).apply();

        if (attempts >= MAX_ATTEMPTS) {
            long lockoutUntil = System.currentTimeMillis() + LOCKOUT_DURATION;
            prefs.edit().putLong(LOCKOUT_UNTIL, lockoutUntil).apply();
        }
    }

    public void recordSuccessfulAttempt() {
        prefs.edit().remove(FAILED_ATTEMPTS).remove(LOCKOUT_UNTIL).apply();
    }

    public boolean isLockedOut() {
        long lockoutUntil = prefs.getLong(LOCKOUT_UNTIL, 0);
        if (lockoutUntil == 0) return false;
        if (System.currentTimeMillis() > lockoutUntil) {
            prefs.edit().remove(LOCKOUT_UNTIL).putInt(FAILED_ATTEMPTS, 0).apply();
            return false;
        }
        return true;
    }

    public long getRemainingLockoutMillis() {
        long lockoutUntil = prefs.getLong(LOCKOUT_UNTIL, 0);
        if (lockoutUntil == 0) return 0;
        return Math.max(0, lockoutUntil - System.currentTimeMillis());
    }
}