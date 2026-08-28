package com.secureline.secureline.util;

import java.util.Arrays;

public class ByteArrayUtils {

    public static byte[] concat(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array != null ? array.length : 0;
        }

        byte[] result = new byte[totalLength];
        int offset = 0;
        for (byte[] array : arrays) {
            if (array != null) {
                System.arraycopy(array, 0, result, offset, array.length);
                offset += array.length;
            }
        }
        return result;
    }

    public static byte[] subArray(byte[] input, int start) {
        return subArray(input, start, input.length - start);
    }

    public static byte[] subArray(byte[] input, int start, int length) {
        if (input == null || start < 0 || length < 0 || start + length > input.length) {
            return null;
        }
        return Arrays.copyOfRange(input, start, start + length);
    }

    public static boolean startsWith(byte[] data, byte[] prefix) {
        if (data == null || prefix == null || data.length < prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (data[i] != prefix[i]) return false;
        }
        return true;
    }

    public static byte[] reverse(byte[] data) {
        if (data == null) return null;
        byte[] reversed = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            reversed[i] = data[data.length - 1 - i];
        }
        return reversed;
    }
}
