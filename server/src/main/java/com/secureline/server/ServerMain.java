package com.secureline.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 100;
    private static long startTime;

    private static ServerLogger logger;
    private static ServerConfig config;
    private static ClientConnectionManager connectionManager;
    private static EncryptedMessageRouter messageRouter;
    private static AuthenticationHandler authHandler;
    private static SessionManager sessionManager;
    private static SecurityManager securityManager;
    private static RateLimiter rateLimiter;
    private static MessageStorageManager storageManager;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

        logger = ServerLogger.getInstance();
        config = new ServerConfig("config.yml");
        connectionManager = new ClientConnectionManager();
        messageRouter = new EncryptedMessageRouter();
        authHandler = new AuthenticationHandler();
        sessionManager = new SessionManager();
        securityManager = new SecurityManager();
        rateLimiter = new RateLimiter(100, 60000);
        storageManager = new MessageStorageManager();

        logger.info("Starting SecureLine Server on port " + config.getServerPort());

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(config.getServerPort())) {
            logger.info("SecureLine Server started successfully");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new ConnectionHandler(clientSocket));
            }
        } catch (Exception e) {
            logger.error("Server error: " + e.getMessage());
        } finally {
            executorService.shutdown();
        }
    }

    public static long getStartTime() {
        return startTime;
    }

    public static ServerLogger getLogger() {
        return logger;
    }

    public static ServerConfig getConfig() {
        return config;
    }

    public static ClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public static EncryptedMessageRouter getMessageRouter() {
        return messageRouter;
    }

    public static AuthenticationHandler getAuthHandler() {
        return authHandler;
    }

    public static SessionManager getSessionManager() {
        return sessionManager;
    }

    public static SecurityManager getSecurityManager() {
        return securityManager;
    }

    public static RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public static MessageStorageManager getStorageManager() {
        return storageManager;
    }
}
