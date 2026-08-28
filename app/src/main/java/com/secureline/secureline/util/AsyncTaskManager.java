package com.secureline.secureline.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncTaskManager {

    private static final ExecutorService executor = Executors.newFixedThreadPool(8);

    public static void execute(Runnable task) {
        executor.execute(task);
    }

    public static void executeOnBackground(Runnable task, Runnable onComplete) {
        executor.execute(() -> {
            task.run();
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public static void shutdown() {
        executor.shutdown();
    }
}
