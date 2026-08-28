package com.secureline.secureline.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MessageCompressor {

    public static byte[] compress(byte[] data) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            GZIPOutputStream gzipOut = new GZIPOutputStream(byteOut);
            gzipOut.write(data);
            gzipOut.close();
            return byteOut.toByteArray();
        } catch (Exception e) {
            return data;
        }
    }

    public static byte[] decompress(byte[] compressedData) {
        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(compressedData);
            GZIPInputStream gzipIn = new GZIPInputStream(byteIn);
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = gzipIn.read(buffer)) != -1) {
                byteOut.write(buffer, 0, bytesRead);
            }
            gzipIn.close();
            return byteOut.toByteArray();
        } catch (Exception e) {
            return compressedData;
        }
    }
}