package com.secureline.secureline.security;

import android.content.Context;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class BiometricAuth {

    private final BiometricPrompt biometricPrompt;
    private final BiometricPrompt.PromptInfo promptInfo;

    public BiometricAuth(FragmentActivity activity, AuthCallback callback) {
        Executor executor = ContextCompat.getMainExecutor(activity);

        biometricPrompt = new BiometricPrompt(activity, executor,
            new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    callback.onSuccess();
                }

                @Override
                public void onAuthenticationFailed() {
                    callback.onFailure("Authentication failed");
                }

                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    callback.onFailure(errString.toString());
                }
            });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
            .setTitle("SecureLine Unlock")
            .setSubtitle("Authenticate to access your messages")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build();
    }

    public void authenticate() {
        biometricPrompt.authenticate(promptInfo);
    }

    public interface AuthCallback {
        void onSuccess();
        void onFailure(String error);
    }
}
