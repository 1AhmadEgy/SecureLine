package com.secureline.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8)
            );
            OutputStream output = clientSocket.getOutputStream();

            String line;
            while ((line = reader.readLine()) != null) {
                String response = processRequest(line);
                output.write((response + "\n").getBytes(StandardCharsets.UTF_8));
                output.flush();
            }
        } catch (Exception e) {
            // Client disconnected
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    private String processRequest(String request) {
        // Route encrypted messages
        return "OK";
    }
}
