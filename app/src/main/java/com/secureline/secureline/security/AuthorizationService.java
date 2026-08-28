package com.secureline.secureline.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuthorizationService {

    private final Map<String, Set<String>> userRoles;

    public AuthorizationService() {
        userRoles = new HashMap<>();
    }

    public void assignRole(String userId, String role) {
        userRoles.computeIfAbsent(userId, k -> new HashSet<>()).add(role);
    }

    public void removeRole(String userId, String role) {
        Set<String> roles = userRoles.get(userId);
        if (roles != null) {
            roles.remove(role);
        }
    }

    public boolean hasRole(String userId, String role) {
        Set<String> roles = userRoles.get(userId);
        return roles != null && roles.contains(role);
    }

    public boolean hasAnyRole(String userId, String... roles) {
        Set<String> userRolesSet = userRoles.get(userId);
        if (userRolesSet == null) return false;
        for (String role : roles) {
            if (userRolesSet.contains(role)) return true;
        }
        return false;
    }

    public Set<String> getRoles(String userId) {
        return userRoles.get(userId);
    }
}
