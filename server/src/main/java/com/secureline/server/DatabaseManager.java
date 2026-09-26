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
        this.url = require(url, "database URL");
        this.username = require(username, "database username");
        this.password = require(password, "database password");
    }

    public Connection getConnection() throws java.sql.SQLException {
        return DriverManager.getConnection(url, username, password);
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
        } catch (java.sql.SQLException e) {
            return false;
        }
    }

    public byte[] fetchEncryptedMessage(String recipientId) {
        String sql = "SELECT encrypted_body FROM messages WHERE recipient_id = ? " +
                     "ORDER BY timestamp LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recipientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("encrypted_body");
                }
                return null;
            }
        } catch (java.sql.SQLException e) {
            return null;
        }
    }

    public boolean deleteMessage(String id) {
        String sql = "DELETE FROM messages WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (java.sql.SQLException e) {
            return false;
        }
    }

    private static String require(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
