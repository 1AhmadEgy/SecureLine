package com.secureline.secureline.security;

import android.content.Context;
import android.content.SharedPreferences;

public class MessageExpiry {

    private static final String PREFS_NAME = "secureline_expiry";
    private final SharedPreferences prefs;

    public MessageExpiry(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setDefaultExpirySeconds(int seconds) {
        prefs.edit().putInt("default_expiry", seconds).apply();
    }

    public int getDefaultExpirySeconds() {
        return prefs.getInt("default_expiry", 0);
    }

    public boolean shouldMessageExpire(long messageTimestamp) {
        int expiry = getDefaultExpirySeconds();
        if (expiry <= 0) return false;
        long currentTime = System.currentTimeMillis();
        return (currentTime - messageTimestamp) >= (expiry * 1000L);
    }

    public void setExpiryForConversation(String conversationId, int seconds) {
        prefs.edit().putInt("expiry_" + conversationId, seconds).apply();
    }

    public int getExpiryForConversation(String conversationId) {
        return prefs.getInt("expiry_" + conversationId, getDefaultExpirySeconds());
    }
}
