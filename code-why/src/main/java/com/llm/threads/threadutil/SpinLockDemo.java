package com.llm.threads.threadutil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SpinLockDemo {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.incrementAndGet();

    }

    public static void testSpinLock(){
        SpinLock spinLock = new SpinLock();

        new Thread(() -> {
            spinLock.lock();
            System.out.println("spinLock 01");
            spinLock.unlock();
        }, "AAA").start();

        new Thread(() -> {
            spinLock.lock();
            System.out.println("spinLock 02");
            spinLock.unlock();
        }, "BBB").start();

        System.out.println("Main");
    }


    static class SpinLock {

        public static volatile AtomicReference<Object> atomicReference = new AtomicReference<>();

        public void lock(){
            Thread currentThread = Thread.currentThread();
            System.out.println(Thread.currentThread().getName() + " lock");
            while (!atomicReference.compareAndSet(null, currentThread)){
                System.out.println("Got lock successful");
            }
        }

        public void unlock(){
            Thread currentThread = Thread.currentThread();
            System.out.println(Thread.currentThread().getName() + " unlock");
            atomicReference.compareAndSet( currentThread, null);
        }

    }
}
