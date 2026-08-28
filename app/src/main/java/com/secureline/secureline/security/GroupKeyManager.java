package com.secureline.secureline.security;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class GroupKeyManager {

    private final Map<String, byte[]> groupKeys;

    public GroupKeyManager() {
        groupKeys = new HashMap<>();
    }

    public void createGroupKey(String groupId) {
        byte[] key = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        groupKeys.put(groupId, key);
    }

    public byte[] getGroupKey(String groupId) {
        return groupKeys.get(groupId);
    }

    public void updateGroupKey(String groupId) {
        createGroupKey(groupId);
    }

    public void removeGroupKey(String groupId) {
        groupKeys.remove(groupId);
    }

    public boolean hasGroupKey(String groupId) {
        return groupKeys.containsKey(groupId);
    }

    public void clearAllGroupKeys() {
        groupKeys.clear();
    }
}
