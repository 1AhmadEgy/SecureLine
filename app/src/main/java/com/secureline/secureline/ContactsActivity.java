package com.secureline.secureline;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.secureline.secureline.database.ContactDao;
import com.secureline.secureline.database.DatabaseManager;
import com.secureline.secureline.security.ScreenshotProtection;
import com.secureline.secureline.util.ConversationUtils;

import net.zetetic.database.sqlcipher.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private static final int QR_SCAN_REQUEST = 100;

    private RecyclerView recyclerContacts;
    private ImageButton btnAddContact;
    private ContactAdapter adapter;
    private List<String> contacts;
    private List<String> contactIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ScreenshotProtection.enable(this);

        recyclerContacts = findViewById(R.id.recycler_contacts);
        btnAddContact = findViewById(R.id.btn_add_contact);

        contacts = new ArrayList<>();
        contactIds = new ArrayList<>();
        adapter = new ContactAdapter(contacts);
        recyclerContacts.setLayoutManager(new LinearLayoutManager(this));
        recyclerContacts.setAdapter(adapter);

        loadContacts();

        btnAddContact.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, QrScanActivity.class);
            startActivityForResult(intent, QR_SCAN_REQUEST);
        });

        adapter.setClickListener((position, view) -> {
            String contactName = contacts.get(position);
            String contactUuid = contactIds.size() > position ? contactIds.get(position) : "unknown";

            android.content.Intent intent = new android.content.Intent(this, ChatActivity.class);
            intent.putExtra("contact_id", contactName);
            intent.putExtra("conversation_id", ConversationUtils.generateConversationId("me", contactUuid));
            startActivity(intent);
        });
    }

    private void loadContacts() {
        DatabaseManager dbManager = DatabaseManager.getInstance(this);
        if (dbManager != null) {
            SQLiteDatabase db = dbManager.getSecureDatabase();
            if (db != null) {
                ContactDao contactDao = new ContactDao(db);
                contacts.clear();
                contactIds.clear();
                for (String contact : contactDao.getAllContacts()) {
                    contacts.add(contact);
                    contactIds.add(contact);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_SCAN_REQUEST && resultCode == RESULT_OK && data != null) {
            String contactData = data.getStringExtra("contact_data");
            if (contactData != null && !contactData.isEmpty()) {
                addContactFromQr(contactData);
            }
        }
    }

    private void addContactFromQr(String contactData) {
        DatabaseManager dbManager = DatabaseManager.getInstance(this);
        if (dbManager != null) {
            SQLiteDatabase db = dbManager.getSecureDatabase();
            if (db != null) {
                ContactDao contactDao = new ContactDao(db);
                long result = contactDao.insertContact(contactData, contactData, contactData);
                if (result > 0) {
                    Toast.makeText(this, "تمت إضافة جهة الاتصال", Toast.LENGTH_SHORT).show();
                    loadContacts();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        ScreenshotProtection.disable(this);
        super.onDestroy();
    }
}
