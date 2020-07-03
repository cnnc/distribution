package com.cnnc.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    private static int count = 0;

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void p() {
        for (int i = 0; i < 5; i++) {

            executorService.submit(() -> {
                RedissonClient client = init();
                try {

                    client.getLock("aaa").lock();
                    count++;
                    System.out.println(Thread.currentThread().getName() + " count = " + count);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    client.getLock("aaa").unlock();
                }
            });
        }
    }

    public static RedissonClient init() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        return Redisson.create(config);
    }

    public static void main(String[] args) throws InterruptedException {
        p();
    }

}
