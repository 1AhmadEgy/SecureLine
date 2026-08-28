package com.secureline.secureline.security;

import java.util.HashMap;
import java.util.Map;

public class TrustStore {

    public enum TrustLevel {
        UNKNOWN,
        UNTRUSTED,
        TRUSTED,
        VERIFIED,
        BLOCKED
    }

    private final Map<String, TrustLevel> trustLevels;

    public TrustStore() {
        trustLevels = new HashMap<>();
    }

    public void setTrustLevel(String identityId, TrustLevel level) {
        trustLevels.put(identityId, level);
    }

    public TrustLevel getTrustLevel(String identityId) {
        TrustLevel level = trustLevels.get(identityId);
        return level != null ? level : TrustLevel.UNKNOWN;
    }

    public boolean isTrusted(String identityId) {
        TrustLevel level = getTrustLevel(identityId);
        return level == TrustLevel.TRUSTED || level == TrustLevel.VERIFIED;
    }

    public boolean isVerified(String identityId) {
        return getTrustLevel(identityId) == TrustLevel.VERIFIED;
    }

    public boolean isBlocked(String identityId) {
        return getTrustLevel(identityId) == TrustLevel.BLOCKED;
    }

    public void verifyIdentity(String identityId) {
        trustLevels.put(identityId, TrustLevel.VERIFIED);
    }

    public void blockIdentity(String identityId) {
        trustLevels.put(identityId, TrustLevel.BLOCKED);
    }

    public void unblockIdentity(String identityId) {
        trustLevels.put(identityId, TrustLevel.UNKNOWN);
    }
}
