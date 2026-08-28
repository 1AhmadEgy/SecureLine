package com.secureline.secureline.network;

public class RetryPolicy {

    private final int maxRetries;
    private final long baseDelayMillis;
    private final long maxDelayMillis;
    private int currentRetry;

    public RetryPolicy(int maxRetries, long baseDelayMillis, long maxDelayMillis) {
        this.maxRetries = maxRetries;
        this.baseDelayMillis = baseDelayMillis;
        this.maxDelayMillis = maxDelayMillis;
        this.currentRetry = 0;
    }

    public boolean shouldRetry() {
        return currentRetry < maxRetries;
    }

    public long getNextDelay() {
        long delay = baseDelayMillis * (long) Math.pow(2, currentRetry);
        currentRetry++;
        return Math.min(delay, maxDelayMillis);
    }

    public void reset() {
        currentRetry = 0;
    }

    public int getCurrentRetry() {
        return currentRetry;
    }
}