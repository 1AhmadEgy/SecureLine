package com.secureline.secureline.security;

import java.util.HashMap;
import java.util.Map;

public class TrustManager {

    public enum TrustLevel {
        UNTRUSTED,
        TRUSTED,
        FULLY_TRUSTED,
        BLOCKED
    }

    private final Map<String, TrustLevel> trustLevels;

    public TrustManager() {
        trustLevels = new HashMap<>();
    }

    public void setTrustLevel(String contactId, TrustLevel level) {
        trustLevels.put(contactId, level);
    }

    public TrustLevel getTrustLevel(String contactId) {
        TrustLevel level = trustLevels.get(contactId);
        return level != null ? level : TrustLevel.UNTRUSTED;
    }

    public boolean isTrusted(String contactId) {
        TrustLevel level = getTrustLevel(contactId);
        return level == TrustLevel.TRUSTED || level == TrustLevel.FULLY_TRUSTED;
    }

    public boolean isBlocked(String contactId) {
        return getTrustLevel(contactId) == TrustLevel.BLOCKED;
    }

    public void block(String contactId) {
        trustLevels.put(contactId, TrustLevel.BLOCKED);
    }

    public void unblock(String contactId) {
        trustLevels.put(contactId, TrustLevel.UNTRUSTED);
    }

    public void clearAll() {
        trustLevels.clear();
    }
}
