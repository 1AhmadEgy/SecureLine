package com.secureline.secureline.util;

public class HexUtils {

    public static String toHex(byte[] data) {
        if (data == null) return null;
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static byte[] fromHex(String hex) {
        if (hex == null || hex.length() % 2 != 0) return null;
        byte[] data = new byte[hex.length() / 2];
        for (int i = 0; i < data.length; i++) {
            int index = i * 2;
            data[i] = (byte) ((Character.digit(hex.charAt(index), 16) << 4) +
                              Character.digit(hex.charAt(index + 1), 16));
        }
        return data;
    }

    public static String toHexWithSpaces(byte[] data, int groupSize) {
        String hex = toHex(data);
        if (hex == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length(); i++) {
            if (i > 0 && i % groupSize == 0) {
                sb.append(" ");
            }
            sb.append(hex.charAt(i));
        }
        return sb.toString();
    }
}
