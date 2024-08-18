package com.llm.threads.lock;

import java.util.concurrent.locks.ReentrantLock;

public class AQSTest {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
    }
}
