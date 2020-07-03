package com.cnnc.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class RedissLockUtil {

    private static RedissonClient redissonClient;

    public static void setRedissonClient(RedissonClient redissonClient) {
        RedissLockUtil.redissonClient = redissonClient;
    }

    public static RLock lock(String key) {
        RLock lock = redissonClient.getLock(key);
        lock.lock();
        return lock;
    }

    public static void unLock(String key) {
        RLock lock = redissonClient.getLock(key);
        lock.unlock();
    }

    public static void unLock(RLock lock) {
        lock.unlock();
    }

    public static RLock lock(String key, int timeout) {
        RLock lock = redissonClient.getLock(key);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }

    public static RLock lock(String key, int timeout, TimeUnit unit) {
        RLock lock = redissonClient.getLock(key);
        lock.lock(timeout, unit);
        return lock;
    }

    public static boolean tryLock(String key, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(key);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return false;
        }
    }

    public static boolean tryLock(String key, int waitTime, int leaseTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(key);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return false;
        }
    }
}
