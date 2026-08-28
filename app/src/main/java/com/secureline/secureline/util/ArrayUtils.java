package com.secureline.secureline.util;

import java.util.Arrays;

public class ArrayUtils {

    public static byte[] concatArrays(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static boolean contains(byte[] array, byte value) {
        if (array == null) return false;
        for (byte b : array) {
            if (b == value) return true;
        }
        return false;
    }

    public static byte[] removeFirst(byte[] array, int count) {
        if (array == null || count >= array.length) return new byte[0];
        return Arrays.copyOfRange(array, count, array.length);
    }

    public static byte[] removeLast(byte[] array, int count) {
        if (array == null || count >= array.length) return new byte[0];
        return Arrays.copyOfRange(array, 0, array.length - count);
    }
}
