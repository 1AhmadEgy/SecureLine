package com.secureline.secureline;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.secureline.secureline.crypto.ObfuscationLayer;
import com.secureline.secureline.network.TorManager;
import com.secureline.secureline.security.KeyManager;
import com.secureline.secureline.security.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText messageInput;
    private Button sendButton;
    private Button torButton;
    private ListView messageList;
    private List<String> messages;
    private TorManager torManager;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        torButton = findViewById(R.id.tor_button);
        messageList = findViewById(R.id.message_list);

        messages = new ArrayList<>();
        torManager = new TorManager();
        sessionManager = new SessionManager(this);

        if (!sessionManager.isValidSession()) {
            sessionManager.createSessionToken();
        }

        KeyManager.getOrCreateDatabaseKey();

        sendButton.setOnClickListener(v -> sendMessage());
        torButton.setOnClickListener(v -> toggleTor());
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (text.isEmpty()) return;

        byte[] data = text.getBytes();
        byte[] obfuscated = ObfuscationLayer.obfuscate(data);

        messages.add(text);
        messageInput.setText("");
        updateList();

        Toast.makeText(this, "Message sent (encrypted)", Toast.LENGTH_SHORT).show();
    }

    private void toggleTor() {
        if (torManager.isTorRunning()) {
            torManager.stop();
            torButton.setText("Tor: OFF");
            Toast.makeText(this, "Tor disabled", Toast.LENGTH_SHORT).show();
        } else {
            torManager.start(this);
            torButton.setText("Tor: ON");
            Toast.makeText(this, "Tor enabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateList() {
        // Update ListView adapter
    }
}
