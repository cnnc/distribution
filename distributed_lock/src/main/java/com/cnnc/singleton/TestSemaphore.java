package com.cnnc.singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class TestSemaphore implements Runnable {

    private int count = 10;

    private Semaphore semaphore = new Semaphore(1);

    public void run() {
        try {
            semaphore.acquire();
            count--;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }

    }

    public static void main(String[] args) {
        TestSemaphore t = new TestSemaphore();
        for(int i=0; i<5; i++) {
            new Thread(t, "THREAD" + i).start();
        }
    }

}
