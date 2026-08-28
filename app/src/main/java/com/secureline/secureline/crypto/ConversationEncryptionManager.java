package com.secureline.secureline.crypto;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class ConversationEncryptionManager {

    private final Map<String, MessageEncryptor> conversationEncryptors;

    public ConversationEncryptionManager() {
        conversationEncryptors = new HashMap<>();
    }

    public void initializeConversation(String conversationId, byte[] key) {
        MessageEncryptor encryptor = new MessageEncryptor(key);
        conversationEncryptors.put(conversationId, encryptor);
    }

    public byte[] encryptForConversation(String conversationId, byte[] plaintext) {
        MessageEncryptor encryptor = conversationEncryptors.get(conversationId);
        if (encryptor == null) return null;
        return encryptor.encrypt(plaintext);
    }

    public byte[] decryptFromConversation(String conversationId, byte[] ciphertext) {
        MessageEncryptor encryptor = conversationEncryptors.get(conversationId);
        if (encryptor == null) return null;
        return encryptor.decrypt(ciphertext);
    }

    public void rotateConversationKey(String conversationId, byte[] newKey) {
        initializeConversation(conversationId, newKey);
    }

    public void removeConversation(String conversationId) {
        conversationEncryptors.remove(conversationId);
    }

    public void clearAllConversations() {
        conversationEncryptors.clear();
    }

    public static byte[] generateConversationKey() {
        byte[] key = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        return key;
    }
}
