package com.secureline.secureline.security;

import android.content.Context;
import java.util.List;

public class SecureEnvironment {

    private static SecureEnvironment instance;
    private final Context context;
    private final SecurityAuditor.AuditResult auditResult;

    private SecureEnvironment(Context context) {
        this.context = context;
        this.auditResult = SecurityAuditor.performAudit(context);
    }

    public static synchronized SecureEnvironment getInstance(Context context) {
        if (instance == null) {
            instance = new SecureEnvironment(context);
        }
        return instance;
    }

    public boolean isSecure() {
        return auditResult.isSecure();
    }

    public SecurityAuditor.AuditResult getAuditResult() {
        return auditResult;
    }

    public List<String> getThreats() {
        return SecurityScanner.scanForThreats(context);
    }
}
