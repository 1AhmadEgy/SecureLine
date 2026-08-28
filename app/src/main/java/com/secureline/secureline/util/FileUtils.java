package com.secureline.secureline.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUtils {

    public static byte[] readFile(File file) {
        try {
            FileInputStream input = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            input.read(data);
            input.close();
            return data;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean writeFile(File file, byte[] data) {
        try {
            FileOutputStream output = new FileOutputStream(file);
            output.write(data);
            output.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteFile(File file) {
        return file != null && file.exists() && file.delete();
    }

    public static boolean deleteDirectory(File directory) {
        if (directory == null || !directory.exists()) return true;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return directory.delete();
    }
}
