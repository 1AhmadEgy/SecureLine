package com.secureline.secureline.security;

import java.util.HashMap;
import java.util.Map;

public class ContactVerificationManager {

    private final Map<String, Boolean> verifiedContacts;

    public ContactVerificationManager() {
        verifiedContacts = new HashMap<>();
    }

    public void markVerified(String contactId) {
        verifiedContacts.put(contactId, true);
    }

    public void markUnverified(String contactId) {
        verifiedContacts.put(contactId, false);
    }

    public boolean isVerified(String contactId) {
        Boolean verified = verifiedContacts.get(contactId);
        return verified != null && verified;
    }

    public void removeVerification(String contactId) {
        verifiedContacts.remove(contactId);
    }

    public void clearAll() {
        verifiedContacts.clear();
    }
}
