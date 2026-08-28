package com.secureline.secureline.security;

public class SecurityContext {

    private byte[] sessionKey;
    private byte[] encryptionKey;
    private byte[] macKey;
    private String userId;
    private boolean isAuthenticated;
    private boolean isEncrypted;

    public SecurityContext() {
        sessionKey = null;
        encryptionKey = null;
        macKey = null;
        userId = null;
        isAuthenticated = false;
        isEncrypted = false;
    }

    public void initialize(byte[] sessionKey, byte[] encryptionKey, byte[] macKey) {
        this.sessionKey = sessionKey;
        this.encryptionKey = encryptionKey;
        this.macKey = macKey;
        this.isEncrypted = true;
    }

    public byte[] getSessionKey() {
        return sessionKey;
    }

    public byte[] getEncryptionKey() {
        return encryptionKey;
    }

    public byte[] getMacKey() {
        return macKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.isAuthenticated = authenticated;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public void clear() {
        if (sessionKey != null) java.util.Arrays.fill(sessionKey, (byte) 0);
        if (encryptionKey != null) java.util.Arrays.fill(encryptionKey, (byte) 0);
        if (macKey != null) java.util.Arrays.fill(macKey, (byte) 0);
        sessionKey = null;
        encryptionKey = null;
        macKey = null;
        userId = null;
        isAuthenticated = false;
        isEncrypted = false;
    }
}
