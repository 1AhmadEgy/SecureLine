package com.secureline.secureline.network;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageDispatcher {

    private final Queue<DispatchTask> taskQueue;
    private boolean isDispatching;

    public MessageDispatcher() {
        taskQueue = new ConcurrentLinkedQueue<>();
        isDispatching = false;
    }

    public void addTask(DispatchTask task) {
        taskQueue.offer(task);
        if (!isDispatching) {
            dispatchNext();
        }
    }

    private void dispatchNext() {
        isDispatching = true;
        DispatchTask task = taskQueue.poll();
        if (task == null) {
            isDispatching = false;
            return;
        }
        task.execute();
        dispatchNext();
    }

    public void stopDispatching() {
        taskQueue.clear();
        isDispatching = false;
    }

    public static class DispatchTask {
        private final Runnable runnable;

        public DispatchTask(Runnable runnable) {
            this.runnable = runnable;
        }

        public void execute() {
            if (runnable != null) {
                runnable.run();
            }
        }
    }
}
