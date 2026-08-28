package com.secureline.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseManager {

    private final String url;
    private final String username;
    private final String password;

    public DatabaseManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean storeEncryptedMessage(String id, String senderId, 
                                          String recipientId, byte[] encryptedBody) {
        String sql = "INSERT INTO messages (id, sender_id, recipient_id, encrypted_body) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, senderId);
            stmt.setString(3, recipientId);
            stmt.setBytes(4, encryptedBody);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public byte[] fetchEncryptedMessage(String recipientId) {
        String sql = "SELECT encrypted_body FROM messages WHERE recipient_id = ? " +
                     "ORDER BY timestamp LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recipientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("encrypted_body");
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteMessage(String id) {
        String sql = "DELETE FROM messages WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
