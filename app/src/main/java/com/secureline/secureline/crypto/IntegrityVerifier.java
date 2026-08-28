package com.secureline.secureline.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

public class IntegrityVerifier {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    public static byte[] sign(byte[] data, byte[] macKey) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(macKey, HMAC_ALGORITHM);
            mac.init(keySpec);
            return mac.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean verify(byte[] data, byte[] macKey, byte[] signature) {
        byte[] computed = sign(data, macKey);
        if (computed == null || signature == null) return false;
        return constantTimeEquals(computed, signature);
    }

    public static byte[] appendSignature(byte[] data, byte[] macKey) {
        byte[] signature = sign(data, macKey);
        if (signature == null) return null;

        byte[] result = new byte[data.length + signature.length];
        System.arraycopy(data, 0, result, 0, data.length);
        System.arraycopy(signature, 0, result, data.length, signature.length);
        return result;
    }

    public static byte[] extractAndVerify(byte[] signedData, byte[] macKey) {
        if (signedData.length < 32) return null;

        int dataLength = signedData.length - 32;
        byte[] data = new byte[dataLength];
        byte[] signature = new byte[32];

        System.arraycopy(signedData, 0, data, 0, dataLength);
        System.arraycopy(signedData, dataLength, signature, 0, 32);

        if (verify(data, macKey, signature)) {
            return data;
        }
        return null;
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
}
