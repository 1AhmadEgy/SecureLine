package com.secureline.server;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerLogger {

    private static final String LOG_FILE = "logs/secureline-server.log";
    private static ServerLogger instance;

    private ServerLogger() {}

    public static synchronized ServerLogger getInstance() {
        if (instance == null) {
            instance = new ServerLogger();
        }
        return instance;
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void warning(String message) {
        log("WARNING", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    public void security(String message) {
        log("SECURITY", message);
    }

    private void log(String level, String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logLine = String.format("[%s] [%s] %s%n", timestamp, level, message);

        System.out.print(logLine);

        try {
            FileWriter writer = new FileWriter(LOG_FILE, true);
            writer.write(logLine);
            writer.close();
        } catch (Exception e) {
            // Ignore file writing errors
        }
    }
}
