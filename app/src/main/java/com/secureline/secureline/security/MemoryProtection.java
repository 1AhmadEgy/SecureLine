package com.secureline.secureline.security;

import java.util.Arrays;

public class MemoryProtection {

    public static void clearBytes(byte[] data) {
        if (data != null) {
            Arrays.fill(data, (byte) 0);
        }
    }

    public static void clearCharArray(char[] data) {
        if (data != null) {
            Arrays.fill(data, '\0');
        }
    }

    public static void clearString(StringBuilder sb) {
        if (sb != null) {
            for (int i = 0; i < sb.length(); i++) {
                sb.setCharAt(i, '\0');
            }
            sb.setLength(0);
        }
    }
}
