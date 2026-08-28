package com.secureline.secureline.util;

import java.util.UUID;

public class ConversationUtils {

    public static String generateConversationId(String userId1, String userId2) {
        if (userId1.compareTo(userId2) < 0) {
            return userId1 + "-" + userId2;
        }
        return userId2 + "-" + userId1;
    }

    public static String generateGroupId() {
        return "group_" + UUID.randomUUID().toString();
    }

    public static String generateMessageId() {
        return "msg_" + UUID.randomUUID().toString();
    }

    public static String generateUserId() {
        return "user_" + UUID.randomUUID().toString();
    }
}
