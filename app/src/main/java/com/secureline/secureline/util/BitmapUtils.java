package com.secureline.secureline.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtils {

    public static Bitmap decodeFromBytes(byte[] data) {
        if (data == null) return null;
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static byte[] encodeToBytes(Bitmap bitmap) {
        if (bitmap == null) return null;
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        return output.toByteArray();
    }

    public static Bitmap resize(Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap == null) return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= maxWidth && height <= maxHeight) return bitmap;

        float ratio = Math.min((float) maxWidth / width, (float) maxHeight / height);
        int newWidth = (int) (width * ratio);
        int newHeight = (int) (height * ratio);

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }
}
