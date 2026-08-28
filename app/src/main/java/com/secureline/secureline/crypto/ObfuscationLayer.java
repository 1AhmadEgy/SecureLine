package com.secureline.secureline.crypto;

import java.security.SecureRandom;
import java.util.Arrays;

public class ObfuscationLayer {

    private static final int DUMMY_PREFIX_SIZE = 8;
    private static final int DUMMY_SUFFIX_SIZE = 12;

    public static byte[] obfuscate(byte[] originalEncryptedData) {
        SecureRandom random = new SecureRandom();

        byte[] reversed = reverseArray(originalEncryptedData);

        byte[] prefix = new byte[DUMMY_PREFIX_SIZE];
        byte[] suffix = new byte[DUMMY_SUFFIX_SIZE];
        random.nextBytes(prefix);
        random.nextBytes(suffix);

        byte[] result = new byte[prefix.length + reversed.length + suffix.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(reversed, 0, result, prefix.length, reversed.length);
        System.arraycopy(suffix, 0, result, prefix.length + reversed.length, suffix.length);

        return result;
    }

    public static byte[] deobfuscate(byte[] obfuscatedData) {
        int dataStart = DUMMY_PREFIX_SIZE;
        int dataEnd = obfuscatedData.length - DUMMY_SUFFIX_SIZE;

        if (dataEnd <= dataStart) {
            return new byte[0];
        }

        byte[] reversed = Arrays.copyOfRange(obfuscatedData, dataStart, dataEnd);

        return reverseArray(reversed);
    }

    private static byte[] reverseArray(byte[] input) {
        byte[] reversed = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            reversed[i] = input[input.length - 1 - i];
        }
        return reversed;
    }
}
