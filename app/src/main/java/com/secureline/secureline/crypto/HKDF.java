package com.secureline.secureline.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;

public class HKDF {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final int HASH_LENGTH = 32;

    public static byte[] extract(byte[] salt, byte[] inputKeyMaterial) {
        try {
            if (salt == null || salt.length == 0) {
                salt = new byte[HASH_LENGTH];
            }
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(salt, HMAC_ALGORITHM));
            return mac.doFinal(inputKeyMaterial);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] expand(byte[] prk, byte[] info, int outputLength) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(prk, HMAC_ALGORITHM));

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] previousBlock = new byte[0];
            int counter = 1;

            while (output.size() < outputLength) {
                mac.reset();
                mac.update(previousBlock);
                if (info != null) {
                    mac.update(info);
                }
                mac.update((byte) counter);
                previousBlock = mac.doFinal();
                output.write(previousBlock, 0, previousBlock.length);
                counter++;
            }

            return java.util.Arrays.copyOf(output.toByteArray(), outputLength);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] deriveKey(byte[] inputKeyMaterial, byte[] salt, byte[] info, int keySize) {
        byte[] prk = extract(salt, inputKeyMaterial);
        if (prk == null) return null;
        return expand(prk, info, keySize);
    }
}
