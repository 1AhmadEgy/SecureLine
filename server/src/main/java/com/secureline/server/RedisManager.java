package com.secureline.server;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisManager {

    private final JedisPool jedisPool;

    public RedisManager(String redisUrl) {
        this.jedisPool = new JedisPool(redisUrl);
    }

    public void cacheSession(String sessionId, String userId) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex("session:" + sessionId, 86400, userId);
        }
    }

    public String getUserIdFromSession(String sessionId) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get("session:" + sessionId);
        }
    }

    public void deleteSession(String sessionId) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del("session:" + sessionId);
        }
    }

    public void cachePublicKey(String userId, byte[] publicKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(("pubkey:" + userId).getBytes(), publicKey);
        }
    }

    public byte[] getPublicKey(String userId) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(("pubkey:" + userId).getBytes());
        }
    }

    public void close() {
        jedisPool.close();
    }
}
