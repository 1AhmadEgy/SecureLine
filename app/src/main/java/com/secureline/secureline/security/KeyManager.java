package com.secureline.secureline.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public final class KeyManager {

    private static final String KEY_ALIAS = "secureline_db_master_key";
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String PREFS = "secureline_key_material";
    private static final String CIPHERTEXT = "database_passphrase_ciphertext";
    private static final String IV = "database_passphrase_iv";
    private static final int PASSPHRASE_BYTES = 32;
    private static final int IV_BYTES = 12;

    private KeyManager() {}

    public static synchronized SecretKey getOrCreateDatabaseKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);

            if (keyStore.containsAlias(KEY_ALIAS)) {
                KeyStore.Entry entry = keyStore.getEntry(KEY_ALIAS, null);
                if (entry instanceof KeyStore.SecretKeyEntry) {
                    return ((KeyStore.SecretKeyEntry) entry).getSecretKey();
                }
            }

            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE
            );
            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build();

            keyGenerator.init(spec);
            return keyGenerator.generateKey();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to initialize Android Keystore", e);
        }
    }

    public static synchronized String getDatabaseKeyAsString(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context is required");
        }

        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String encodedCiphertext = prefs.getString(CIPHERTEXT, null);
        String encodedIv = prefs.getString(IV, null);

        try {
            SecretKey masterKey = getOrCreateDatabaseKey();

            if (encodedCiphertext != null && encodedIv != null) {
                byte[] plaintext = decrypt(
                    masterKey,
                    Base64.decode(encodedCiphertext, Base64.DEFAULT),
                    Base64.decode(encodedIv, Base64.DEFAULT)
                );
                return Base64.encodeToString(plaintext, Base64.NO_WRAP);
            }

            byte[] passphrase = new byte[PASSPHRASE_BYTES];
            new SecureRandom().nextBytes(passphrase);

            byte[] iv = new byte[IV_BYTES];
            new SecureRandom().nextBytes(iv);
            byte[] ciphertext = encrypt(masterKey, passphrase, iv);

            prefs.edit()
                .putString(CIPHERTEXT, Base64.encodeToString(ciphertext, Base64.NO_WRAP))
                .putString(IV, Base64.encodeToString(iv, Base64.NO_WRAP))
                .apply();

            return Base64.encodeToString(passphrase, Base64.NO_WRAP);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to initialize encrypted database key material", e);
        }
    }

    private static byte[] encrypt(SecretKey key, byte[] plaintext, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(
            Cipher.ENCRYPT_MODE,
            key,
            new GCMParameterSpec(128, iv)
        );
        return cipher.doFinal(plaintext);
    }

    private static byte[] decrypt(SecretKey key, byte[] ciphertext, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(
            Cipher.DECRYPT_MODE,
            key,
            new GCMParameterSpec(128, iv)
        );
        return cipher.doFinal(ciphertext);
    }
}
