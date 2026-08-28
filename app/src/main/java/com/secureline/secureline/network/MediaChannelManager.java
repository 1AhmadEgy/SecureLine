package com.secureline.secureline.network;

import com.secureline.secureline.webrtc.MediaEncryptor;

public class MediaChannelManager {

    private MediaEncryptor mediaEncryptor;
    private byte[] channelKey;

    public void initializeChannel(byte[] key) {
        this.channelKey = key;
        this.mediaEncryptor = new MediaEncryptor(key);
    }

    public byte[] encryptMedia(byte[] mediaData) {
        if (mediaEncryptor == null) return null;
        return mediaEncryptor.encryptMedia(mediaData);
    }

    public byte[] decryptMedia(byte[] encryptedData) {
        if (mediaEncryptor == null) return null;
        return mediaEncryptor.decryptMedia(encryptedData);
    }

    public void rotateChannelKey(byte[] newKey) {
        this.channelKey = newKey;
        this.mediaEncryptor = new MediaEncryptor(newKey);
    }

    public byte[] getChannelKey() {
        return channelKey;
    }
}
