package com.secureline.secureline.security;

import com.secureline.secureline.crypto.AESGCM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SecureFileManager {

    private final byte[] encryptionKey;

    public SecureFileManager(byte[] key) {
        this.encryptionKey = key;
    }

    public boolean saveEncryptedFile(String filePath, byte[] data) {
        byte[] encrypted = AESGCM.encrypt(data, encryptionKey);
        if (encrypted == null) return false;

        try {
            FileOutputStream output = new FileOutputStream(filePath);
            output.write(encrypted);
            output.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public byte[] loadEncryptedFile(String filePath) {
        try {
            File file = new File(filePath);
            byte[] encrypted = new byte[(int) file.length()];
            FileInputStream input = new FileInputStream(file);
            input.read(encrypted);
            input.close();
            return AESGCM.decrypt(encrypted, encryptionKey);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.delete();
    }
}
