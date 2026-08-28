package com.secureline.secureline.security;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class SecureChannelManager {

    private final Map<String, ChannelEncryption> channels;

    public SecureChannelManager() {
        channels = new HashMap<>();
    }

    public void createChannel(String channelId, byte[] key) {
        ChannelEncryption encryption = new ChannelEncryption(key);
        channels.put(channelId, encryption);
    }

    public byte[] encryptForChannel(String channelId, byte[] data) {
        ChannelEncryption encryption = channels.get(channelId);
        if (encryption == null) return null;
        return encryption.encryptChannelData(data);
    }

    public byte[] decryptFromChannel(String channelId, byte[] data) {
        ChannelEncryption encryption = channels.get(channelId);
        if (encryption == null) return null;
        return encryption.decryptChannelData(data);
    }

    public void removeChannel(String channelId) {
        channels.remove(channelId);
    }

    public void clearAllChannels() {
        channels.clear();
    }

    public static byte[] generateChannelKey() {
        byte[] key = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        return key;
    }
}
