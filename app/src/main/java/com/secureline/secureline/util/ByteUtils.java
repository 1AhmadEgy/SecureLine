package com.secureline.secureline.util;

public class ByteUtils {

    public static byte[] concat(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }
        byte[] result = new byte[totalLength];
        int offset = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static byte[] subArray(byte[] input, int start, int length) {
        byte[] result = new byte[length];
        System.arraycopy(input, start, result, 0, length);
        return result;
    }

    public static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }

    public static byte[] randomBytes(int size) {
        byte[] bytes = new byte[size];
        new java.security.SecureRandom().nextBytes(bytes);
        return bytes;
    }
}
