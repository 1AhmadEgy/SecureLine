package com.secureline.secureline;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.secureline.secureline.network.TorManager;
import com.secureline.secureline.security.DataWipeManager;
import com.secureline.secureline.webrtc.CallQualityManager;

import java.util.concurrent.Executor;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat switchTor;
    private SwitchCompat switchEncryptDb;
    private SwitchCompat switchBiometric;
    private SwitchCompat switchScreenshot;
    private RadioGroup radioCallQuality;
    private Button btnLogout;
    private Button btnWipeData;

    private TorManager torManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchTor = findViewById(R.id.switch_tor);
        switchEncryptDb = findViewById(R.id.switch_encrypt_db);
        switchBiometric = findViewById(R.id.switch_biometric);
        switchScreenshot = findViewById(R.id.switch_screenshot);
        radioCallQuality = findViewById(R.id.radio_call_quality);
        btnLogout = findViewById(R.id.btn_logout);
        btnWipeData = findViewById(R.id.btn_wipe_data);

        torManager = new TorManager();

        switchTor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                torManager.start(this);
                Toast.makeText(this, "جارِ الاتصال بشبكة Tor...", Toast.LENGTH_SHORT).show();
            } else {
                torManager.stop();
                Toast.makeText(this, "تم إيقاف Tor", Toast.LENGTH_SHORT).show();
            }
        });

        switchBiometric.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                enableBiometric();
            }
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new android.content.Intent(this, LoginActivity.class));
            finish();
        });

        btnWipeData.setOnClickListener(v -> {
            DataWipeManager.wipeAllData(this);
            Toast.makeText(this, "تم حذف جميع البيانات", Toast.LENGTH_SHORT).show();
            startActivity(new android.content.Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void enableBiometric() {
        BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
            BiometricManager.BIOMETRIC_SUCCESS) {

            Executor executor = ContextCompat.getMainExecutor(this);
            BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        Toast.makeText(SettingsActivity.this, "تم تفعيل القفل بالبصمة", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        switchBiometric.setChecked(false);
                    }
                });

            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("تفعيل القفل بالبصمة")
                .setSubtitle("استخدم بصمتك لقفل التطبيق")
                .setNegativeButtonText("إلغاء")
                .build();

            biometricPrompt.authenticate(promptInfo);
        } else {
            switchBiometric.setChecked(false);
            Toast.makeText(this, "البصمة غير متاحة على هذا الجهاز", Toast.LENGTH_SHORT).show();
        }
    }
}
