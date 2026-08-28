package com.secureline.secureline.security;

import java.util.HashMap;
import java.util.Map;

public class AccessControlManager {

    private final Map<String, Map<String, Boolean>> accessPolicies;

    public AccessControlManager() {
        accessPolicies = new HashMap<>();
    }

    public void grantAccess(String userId, String resource, boolean allowed) {
        accessPolicies.computeIfAbsent(userId, k -> new HashMap<>())
                      .put(resource, allowed);
    }

    public boolean checkAccess(String userId, String resource) {
        Map<String, Boolean> userPolicies = accessPolicies.get(userId);
        if (userPolicies == null) return false;
        Boolean allowed = userPolicies.get(resource);
        return allowed != null && allowed;
    }

    public void revokeAccess(String userId, String resource) {
        Map<String, Boolean> userPolicies = accessPolicies.get(userId);
        if (userPolicies != null) {
            userPolicies.remove(resource);
        }
    }

    public void revokeAllAccess(String userId) {
        accessPolicies.remove(userId);
    }
}
