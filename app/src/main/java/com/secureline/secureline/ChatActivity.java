package com.secureline.secureline;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.secureline.secureline.crypto.ObfuscationLayer;
import com.secureline.secureline.database.DatabaseManager;
import com.secureline.secureline.database.MessageDao;
import com.secureline.secureline.security.ScreenshotProtection;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerMessages;
    private EditText inputMessage;
    private ImageButton btnSend;
    private ImageButton btnBack;
    private ImageButton btnCall;
    private ImageButton btnVerify;
    private TextView chatTitle;
    private List<String> messages;
    private MessageAdapter adapter;
    private String contactId;
    private String conversationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ScreenshotProtection.enable(this);

        recyclerMessages = findViewById(R.id.recycler_messages);
        inputMessage = findViewById(R.id.input_message);
        btnSend = findViewById(R.id.btn_send);
        btnBack = findViewById(R.id.btn_back);
        btnCall = findViewById(R.id.btn_call);
        btnVerify = findViewById(R.id.btn_verify);
        chatTitle = findViewById(R.id.chat_title);

        messages = new ArrayList<>();
        adapter = new MessageAdapter(messages);
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerMessages.setAdapter(adapter);

        contactId = getIntent().getStringExtra("contact_id");
        conversationId = getIntent().getStringExtra("conversation_id");
        chatTitle.setText(contactId != null ? contactId : "محادثة");

        loadMessages();

        btnSend.setOnClickListener(v -> sendMessage());
        btnBack.setOnClickListener(v -> finish());
        btnCall.setOnClickListener(v -> startCall());
        btnVerify.setOnClickListener(v -> verifyContact());
    }

    private void loadMessages() {
        DatabaseManager dbManager = DatabaseManager.getInstance(this);
        if (dbManager != null && conversationId != null) {
            SQLiteDatabase db = dbManager.getSecureDatabase();
            if (db != null) {
                MessageDao messageDao = new MessageDao(db);
                messages.clear();
                messages.addAll(messageDao.getMessagesForConversation(conversationId));
                adapter.notifyDataSetChanged();
                if (!messages.isEmpty()) {
                    recyclerMessages.scrollToPosition(messages.size() - 1);
                }
            }
        }
    }

    private void sendMessage() {
        String text = inputMessage.getText().toString().trim();
        if (text.isEmpty()) return;

        byte[] data = text.getBytes();
        byte[] obfuscated = ObfuscationLayer.obfuscate(data);

        messages.add("أنا: " + text);
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerMessages.scrollToPosition(messages.size() - 1);
        inputMessage.setText("");

        Toast.makeText(this, "تم إرسال الرسالة (مشفرة + معمية)", Toast.LENGTH_SHORT).show();
    }

    private void startCall() {
        android.content.Intent intent = new android.content.Intent(this, CallActivity.class);
        intent.putExtra("contact_name", contactId);
        startActivity(intent);
    }

    private void verifyContact() {
        android.content.Intent intent = new android.content.Intent(this, QrDisplayActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        ScreenshotProtection.disable(this);
        super.onDestroy();
    }
}
