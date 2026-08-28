package com.secureline.secureline.security;

public class SecurityPolicyManager {

    private boolean encryptionRequired;
    private boolean torEnabled;
    private boolean biometricRequired;
    private boolean screenshotBlocked;
    private int minimumKeySize;
    private int sessionTimeoutMinutes;

    public SecurityPolicyManager() {
        encryptionRequired = true;
        torEnabled = false;
        biometricRequired = false;
        screenshotBlocked = true;
        minimumKeySize = 256;
        sessionTimeoutMinutes = 30;
    }

    public boolean isEncryptionRequired() {
        return encryptionRequired;
    }

    public void setEncryptionRequired(boolean required) {
        this.encryptionRequired = required;
    }

    public boolean isTorEnabled() {
        return torEnabled;
    }

    public void setTorEnabled(boolean enabled) {
        this.torEnabled = enabled;
    }

    public boolean isBiometricRequired() {
        return biometricRequired;
    }

    public void setBiometricRequired(boolean required) {
        this.biometricRequired = required;
    }

    public boolean isScreenshotBlocked() {
        return screenshotBlocked;
    }

    public void setScreenshotBlocked(boolean blocked) {
        this.screenshotBlocked = blocked;
    }

    public int getMinimumKeySize() {
        return minimumKeySize;
    }

    public int getSessionTimeoutMinutes() {
        return sessionTimeoutMinutes;
    }
}
