package com.secureline.server;

import java.io.FileInputStream;
import java.util.Properties;

public class ServerConfig {

    private final Properties properties;

    public ServerConfig(String configFilePath) {
        properties = new Properties();
        try {
            FileInputStream input = new FileInputStream(configFilePath);
            properties.load(input);
            input.close();
        } catch (Exception e) {
            setDefaults();
        }
    }

    private void setDefaults() {
        properties.setProperty("server.port", "8080");
        properties.setProperty("database.url", "jdbc:postgresql://localhost:5432/secureline_db");
        properties.setProperty("database.username", "secureline");
        properties.setProperty("database.password", "SecureLine2024!");
        properties.setProperty("redis.url", "redis://localhost:6379");
        properties.setProperty("security.zkSecret", "default-secret-key");
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
