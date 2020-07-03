package com.cnnc.mysql;

public interface Lock {

    void lock();

    boolean tryLock();

    void unlock();

}
