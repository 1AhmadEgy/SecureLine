package com.secureline.server;

import java.util.Properties;

public class ServerConfig {

    private final Properties properties = new Properties();

    public ServerConfig() {
        setDefaults();
    }

    private void setDefaults() {
        properties.setProperty("server.port", env("SECURELINE_SERVER_PORT", "8080"));
        properties.setProperty("database.url", env("SECURELINE_DATABASE_URL", ""));
        properties.setProperty("database.username", env("SECURELINE_DATABASE_USERNAME", ""));
        properties.setProperty("database.password", env("SECURELINE_DATABASE_PASSWORD", ""));
        properties.setProperty("redis.url", env("SECURELINE_REDIS_URL", ""));
        properties.setProperty("security.zkSecret", env("SECURELINE_ZK_SECRET", ""));
    }

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null ? defaultValue : value.trim();
    }

    public int getServerPort() {
        return Integer.parseInt(properties.getProperty("server.port", "8080"));
    }

    public String getDatabaseUrl() {
        return properties.getProperty("database.url", "");
    }

    public String getDatabaseUsername() {
        return properties.getProperty("database.username", "");
    }

    public String getDatabasePassword() {
        return properties.getProperty("database.password", "");
    }

    public String getRedisUrl() {
        return properties.getProperty("redis.url", "");
    }

    public String getZkSecret() {
        return properties.getProperty("security.zkSecret", "");
    }
}
