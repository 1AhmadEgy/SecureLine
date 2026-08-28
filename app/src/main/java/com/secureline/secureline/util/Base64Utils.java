package com.secureline.secureline.util;

import android.util.Base64;

public class Base64Utils {

    public static String encode(byte[] data) {
        if (data == null) return null;
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    public static byte[] decode(String encoded) {
        if (encoded == null) return null;
        try {
            return Base64.decode(encoded, Base64.NO_WRAP);
        } catch (Exception e) {
            return null;
        }
    }

    public static String encodeUrlSafe(byte[] data) {
        if (data == null) return null;
        return Base64.encodeToString(data, Base64.URL_SAFE | Base64.NO_WRAP);
    }

    public static byte[] decodeUrlSafe(String encoded) {
        if (encoded == null) return null;
        try {
            return Base64.decode(encoded, Base64.URL_SAFE | Base64.NO_WRAP);
        } catch (Exception e) {
            return null;
        }
    }
}
