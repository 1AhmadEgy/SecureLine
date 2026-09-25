package com.secureline.server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Server configuration with environment-variable overrides.
 *
 * Secrets must never be committed to the repository. Environment variables
 * take precedence over optional local properties.
 */
public final class ServerConfig {

    private final Properties properties = new Properties();

    public ServerConfig(String configFilePath) {
        loadOptionalProperties(configFilePath);
        applyEnvironmentOverrides();
        validate();
    }

    private void loadOptionalProperties(String configFilePath) {
        if (configFilePath == null || configFilePath.isBlank()) {
            return;
        }

        try (InputStream input = new FileInputStream(configFilePath)) {
            properties.load(input);
        } catch (Exception ignored) {
            // Local properties are optional; production configuration comes from env.
        }
    }

    private void applyEnvironmentOverrides() {
        setFromEnv("server.port", "SECURELINE_PORT");
        setFromEnv("database.url", "SECURELINE_DATABASE_URL");
        setFromEnv("database.username", "SECURELINE_DATABASE_USER");
        setFromEnv("database.password", "SECURELINE_DATABASE_PASSWORD");
        setFromEnv("redis.url", "SECURELINE_REDIS_URL");
        setFromEnv("security.zkSecret", "SECURELINE_ZK_SECRET");
    }

    private void setFromEnv(String property, String environmentVariable) {
        String value = System.getenv(environmentVariable);
        if (value != null && !value.isBlank()) {
            properties.setProperty(property, value);
        }
    }

    private void validate() {
        require("database.password", "SECURELINE_DATABASE_PASSWORD");
        require("security.zkSecret", "SECURELINE_ZK_SECRET");

        properties.putIfAbsent("server.port", "8080");
        properties.putIfAbsent(
            "database.url",
            "jdbc:postgresql://localhost:5432/secureline_db"
        );
        properties.putIfAbsent("database.username", "secureline");
        properties.putIfAbsent("redis.url", "redis://localhost:6379");
    }

    private void require(String property, String environmentVariable) {
        String value = properties.getProperty(property);
        if (value == null || value.isBlank()
                || value.toLowerCase().contains("default-secret")
                || value.contains("SecureLine2024!")) {
            throw new IllegalStateException(
                "Missing secure configuration: set " + environmentVariable
            );
        }
    }

    public int getServerPort() {
        return Integer.parseInt(properties.getProperty("server.port", "8080"));
    }

    public String getDatabaseUrl() {
        return properties.getProperty("database.url");
    }

    public String getDatabaseUsername() {
        return properties.getProperty("database.username");
    }

    public String getDatabasePassword() {
        return properties.getProperty("database.password");
    }

    public String getRedisUrl() {
        return properties.getProperty("redis.url");
    }

    public String getZkSecret() {
        return properties.getProperty("security.zkSecret");
    }
}
