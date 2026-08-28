package com.secureline.secureline.crypto;

public class Steganography {

    public static byte[] hideData(byte[] coverImage, byte[] secretData) {
        if (coverImage.length < secretData.length * 8 + 32) return null;

        byte[] result = coverImage.clone();
        byte[] dataWithLength = new byte[4 + secretData.length];
        dataWithLength[0] = (byte) ((secretData.length >> 24) & 0xFF);
        dataWithLength[1] = (byte) ((secretData.length >> 16) & 0xFF);
        dataWithLength[2] = (byte) ((secretData.length >> 8) & 0xFF);
        dataWithLength[3] = (byte) (secretData.length & 0xFF);
        System.arraycopy(secretData, 0, dataWithLength, 4, secretData.length);

        int dataOffset = 32;
        for (int i = 0; i < dataWithLength.length; i++) {
            byte data = dataWithLength[i];
            for (int bit = 0; bit < 8; bit++) {
                int byteIndex = dataOffset + i * 8 + bit;
                if (byteIndex >= result.length) return null;
                result[byteIndex] = (byte) ((result[byteIndex] & 0xFE) | ((data >> bit) & 1));
            }
        }
        return result;
    }

    public static byte[] extractData(byte[] stegoImage) {
        int dataOffset = 32;
        byte[] lengthBytes = new byte[4];

        for (int i = 0; i < 4; i++) {
            byte data = 0;
            for (int bit = 0; bit < 8; bit++) {
                int byteIndex = dataOffset + i * 8 + bit;
                if (byteIndex >= stegoImage.length) return null;
                data |= (byte) ((stegoImage[byteIndex] & 1) << bit);
            }
            lengthBytes[i] = data;
        }

        int dataLength = ((lengthBytes[0] & 0xFF) << 24) |
                         ((lengthBytes[1] & 0xFF) << 16) |
                         ((lengthBytes[2] & 0xFF) << 8) |
                         (lengthBytes[3] & 0xFF);

        byte[] secretData = new byte[dataLength];
        for (int i = 0; i < dataLength; i++) {
            byte data = 0;
            for (int bit = 0; bit < 8; bit++) {
                int byteIndex = dataOffset + (4 + i) * 8 + bit;
                if (byteIndex >= stegoImage.length) return null;
                data |= (byte) ((stegoImage[byteIndex] & 1) << bit);
            }
            secretData[i] = data;
        }
        return secretData;
    }
}
