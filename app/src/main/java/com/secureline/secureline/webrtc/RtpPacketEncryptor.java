package com.secureline.secureline.webrtc;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class RtpPacketEncryptor {

    private static final int IV_SIZE = 12;
    private static final int TAG_BITS = 128;

    private final byte[] encryptionKey;

    public RtpPacketEncryptor(byte[] key) {
        this.encryptionKey = key;
    }

    public byte[] encryptRtpPacket(byte[] rtpPacket, int sequenceNumber) {
        try {
            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(intToBytes(sequenceNumber), 0, iv, 0, 4);
            SecureRandom random = new SecureRandom();
            byte[] randomPart = new byte[8];
            random.nextBytes(randomPart);
            System.arraycopy(randomPart, 0, iv, 4, 8);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);

            byte[] encrypted = cipher.doFinal(rtpPacket);
            byte[] result = new byte[4 + randomPart.length + encrypted.length];
            System.arraycopy(intToBytes(sequenceNumber), 0, result, 0, 4);
            System.arraycopy(randomPart, 0, result, 4, randomPart.length);
            System.arraycopy(encrypted, 0, result, 4 + randomPart.length, encrypted.length);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public byte[] decryptRtpPacket(byte[] encryptedPacket) {
        try {
            int sequenceNumber = bytesToInt(encryptedPacket, 0);
            byte[] randomPart = new byte[8];
            System.arraycopy(encryptedPacket, 4, randomPart, 0, 8);

            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(intToBytes(sequenceNumber), 0, iv, 0, 4);
            System.arraycopy(randomPart, 0, iv, 4, 8);

            byte[] encrypted = new byte[encryptedPacket.length - 12];
            System.arraycopy(encryptedPacket, 12, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            return null;
        }
    }

    private byte[] intToBytes(int value) {
        return new byte[]{
            (byte) ((value >> 24) & 0xFF),
            (byte) ((value >> 16) & 0xFF),
            (byte) ((value >> 8) & 0xFF),
            (byte) (value & 0xFF)
        };
    }

    private int bytesToInt(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xFF) << 24) |
               ((bytes[offset + 1] & 0xFF) << 16) |
               ((bytes[offset + 2] & 0xFF) << 8) |
               (bytes[offset + 3] & 0xFF);
    }
}
