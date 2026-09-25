package com.secureline.server;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Runtime configuration for the SecureLine Java server.
 *
 * Environment variables take precedence over the optional local properties file.
 * Sensitive values intentionally have no hard-coded production fallback.
 */
public class ServerConfig {

    private final Properties properties;

    public ServerConfig(String configFilePath) {
        properties = new Properties();
        loadFileIfPresent(configFilePath);
        applyEnvironmentOverrides();
    }

    private void loadFileIfPresent(String configFilePath) {
        if (configFilePath == null || configFilePath.isBlank()) {
            return;
        }

        try (FileInputStream input = new FileInputStream(configFilePath)) {
            properties.load(input);
        } catch (Exception ignored) {
            // A local config file is optional. Required secrets are validated below.
        }
    }

    private void applyEnvironmentOverrides() {
        override("server.port", "SECURELINE_SERVER_PORT");
        override("database.url", "SECURELINE_DATABASE_URL");
        override("database.username", "SECURELINE_DATABASE_USERNAME");
        override("database.password", "SECURELINE_DATABASE_PASSWORD");
        override("redis.url", "SECURELINE_REDIS_URL");
        override("security.zkSecret", "SECURELINE_ZK_SECRET");
    }

    private void override(String property, String environmentVariable) {
        String value = System.getenv(environmentVariable);
        if (value != null && !value.isBlank()) {
            properties.setProperty(property, value);
        }
    }

    public int getServerPort() {
        return Integer.parseInt(properties.getProperty("server.port", "8080"));
    }

    public String getDatabaseUrl() {
        return required("database.url", "SECURELINE_DATABASE_URL");
    }

    public String getDatabaseUsername() {
        return required("database.username", "SECURELINE_DATABASE_USERNAME");
    }

    public String getDatabasePassword() {
        return required("database.password", "SECURELINE_DATABASE_PASSWORD");
    }

    public String getRedisUrl() {
        return required("redis.url", "SECURELINE_REDIS_URL");
    }

    public String getZkSecret() {
        return required("security.zkSecret", "SECURELINE_ZK_SECRET");
    }

    private String required(String property, String environmentVariable) {
        String value = properties.getProperty(property);
        if (value == null || value.isBlank()
                || value.equals("SecureLine2024!")
                || value.equals("default-secret-key")) {
            throw new IllegalStateException(
                "Missing required secure configuration: " + environmentVariable
            );
        }
        return value;
    }
}
