package com.secureline.secureline.security;

import android.content.Context;
import android.os.Build;

public class SecurityAuditor {

    public static AuditResult performAudit(Context context) {
        AuditResult result = new AuditResult();

        result.isRooted = RootDetection.isDeviceRooted();
        result.isEmulator = EmulatorDetection.isEmulator();
        result.isDebugged = AntiDebugging.isDebuggerConnected();
        result.isBeingTraced = AntiDebugging.isBeingTraced();

        return result;
    }

    public static class AuditResult {
        public boolean isRooted;
        public boolean isEmulator;
        public boolean isDebugged;
        public boolean isBeingTraced;

        public boolean isSecure() {
            return !isRooted && !isEmulator && !isDebugged && !isBeingTraced;
        }
    }
}