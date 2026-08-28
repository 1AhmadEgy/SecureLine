package com.secureline.secureline.security;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.security.KeyStore;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyManager {

    private static final String TAG = "SecureLine-Key";
    private static final String KEY_ALIAS = "secureline_db_master_key";
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";

    public static SecretKey getOrCreateDatabaseKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);

            if (keyStore.containsAlias(KEY_ALIAS)) {
                return ((KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null)).getSecretKey();
            }

            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE
            );

            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
            )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256);

            keyGenerator.init(builder.build());
            return keyGenerator.generateKey();

        } catch (Exception e) {
            Log.e(TAG, "Error getting database key: " + e.getMessage());
            return null;
        }
    }

    public static String getDatabaseKeyAsString() {
        SecretKey key = getOrCreateDatabaseKey();
        if (key != null) {
            byte[] encoded = key.getEncoded();
            return Base64.encodeToString(encoded, Base64.NO_WRAP);
        }
        return null;
    }
}
