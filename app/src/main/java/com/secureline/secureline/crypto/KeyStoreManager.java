package com.secureline.secureline.crypto;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.security.KeyStore;
import java.security.KeyStoreException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyStoreManager {

    private static final String KEYSTORE_TYPE = "AndroidKeyStore";

    public static SecretKey generateAesKey(String alias) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_TYPE
            );

            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
            )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build();

            keyGenerator.init(spec);
            return keyGenerator.generateKey();
        } catch (Exception e) {
            return null;
        }
    }

    public static SecretKey getKey(String alias) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            keyStore.load(null);
            return ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean deleteKey(String alias) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            keyStore.load(null);
            keyStore.deleteEntry(alias);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean containsKey(String alias) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            keyStore.load(null);
            return keyStore.containsAlias(alias);
        } catch (Exception e) {
            return false;
        }
    }
}
