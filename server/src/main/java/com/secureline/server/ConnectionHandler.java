package com.secureline.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ConnectionHandler implements Runnable {

    private final Socket clientSocket;
    private final ServerLogger logger;

    public ConnectionHandler(Socket socket) {
        this.clientSocket = socket;
        this.logger = ServerLogger.getInstance();
    }

    @Override
    public void run() {
        logger.info("New client connection: " + clientSocket.getInetAddress());

        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8)
            );
            OutputStream output = clientSocket.getOutputStream();

            String line;
            while ((line = reader.readLine()) != null) {
                String response = handleRequest(line);
                output.write((response + "\n").getBytes(StandardCharsets.UTF_8));
                output.flush();
            }
        } catch (Exception e) {
            logger.warning("Client disconnected: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    private String handleRequest(String request) {
        return "OK";
    }
}
