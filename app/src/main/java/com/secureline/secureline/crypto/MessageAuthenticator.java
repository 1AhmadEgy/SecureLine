package com.secureline.secureline.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class MessageAuthenticator {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final int NONCE_SIZE = 16;

    public static byte[] createAuthenticatedMessage(byte[] message, byte[] authKey) {
        byte[] nonce = new byte[NONCE_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(nonce);

        byte[] hmac = computeHmac(message, nonce, authKey);
        if (hmac == null) return null;

        byte[] result = new byte[nonce.length + message.length + hmac.length];
        System.arraycopy(nonce, 0, result, 0, nonce.length);
        System.arraycopy(message, 0, result, nonce.length, message.length);
        System.arraycopy(hmac, 0, result, nonce.length + message.length, hmac.length);
        return result;
    }

    public static byte[] verifyAndExtract(byte[] authenticatedMessage, byte[] authKey) {
        if (authenticatedMessage.length < NONCE_SIZE + 32) return null;

        byte[] nonce = new byte[NONCE_SIZE];
        System.arraycopy(authenticatedMessage, 0, nonce, 0, NONCE_SIZE);

        int messageLength = authenticatedMessage.length - NONCE_SIZE - 32;
        byte[] message = new byte[messageLength];
        System.arraycopy(authenticatedMessage, NONCE_SIZE, message, 0, messageLength);

        byte[] receivedHmac = new byte[32];
        System.arraycopy(authenticatedMessage, NONCE_SIZE + messageLength, receivedHmac, 0, 32);

        byte[] computedHmac = computeHmac(message, nonce, authKey);
        if (computedHmac == null) return null;

        if (!java.security.MessageDigest.isEqual(receivedHmac, computedHmac)) {
            return null;
        }
        return message;
    }

    private static byte[] computeHmac(byte[] message, byte[] nonce, byte[] authKey) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(authKey, HMAC_ALGORITHM);
            mac.init(keySpec);
            mac.update(nonce);
            return mac.doFinal(message);
        } catch (Exception e) {
            return null;
        }
    }
}