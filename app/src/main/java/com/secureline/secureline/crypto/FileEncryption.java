package com.secureline.secureline.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryption {

    private static final int IV_SIZE = 12;
    private static final int TAG_BITS = 128;
    private static final int BUFFER_SIZE = 8192;

    public static boolean encryptFile(File inputFile, File outputFile, byte[] key) {
        try {
            byte[] iv = new byte[IV_SIZE];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);

            FileOutputStream fileOut = new FileOutputStream(outputFile);
            fileOut.write(iv);

            FileInputStream fileIn = new FileInputStream(inputFile);
            CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                cipherOut.write(buffer, 0, bytesRead);
            }

            cipherOut.close();
            fileIn.close();
            fileOut.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean decryptFile(File inputFile, File outputFile, byte[] key) {
        try {
            FileInputStream fileIn = new FileInputStream(inputFile);
            byte[] iv = new byte[IV_SIZE];
            fileIn.read(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
            FileOutputStream fileOut = new FileOutputStream(outputFile);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = cipherIn.read(buffer)) != -1) {
                fileOut.write(buffer, 0, bytesRead);
            }

            fileOut.close();
            cipherIn.close();
            fileIn.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
