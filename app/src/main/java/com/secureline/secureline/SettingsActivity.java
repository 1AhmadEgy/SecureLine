package com.secureline.secureline;

import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.secureline.secureline.network.TorManager;
import com.secureline.secureline.security.KeyManager;

public class SettingsActivity extends AppCompatActivity {

    private Switch torSwitch;
    private Switch encryptDbSwitch;
    private TorManager torManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        torSwitch = findViewById(R.id.tor_switch);
        encryptDbSwitch = findViewById(R.id.encrypt_db_switch);
        torManager = new TorManager();

        torSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                torManager.start(this);
            } else {
                torManager.stop();
            }
        });

        encryptDbSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                KeyManager.getOrCreateDatabaseKey();
            }
        });
    }
}
