package com.llm.threads.lock;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class CASDemo {

    static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 1);

    public static void testCAS(){
        System.out.println("ABA issue started");

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "ABA started");
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
            System.out.println(Thread.currentThread().getName() + "ABA ended");
        }, "t1").start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "ABA started");
            atomicReference.compareAndSet(100, 2019);
            System.out.println(Thread.currentThread().getName() + "ABA ended");
        }, "t2").start();

        //resolved ABA issue
        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();

            System.out.println("First stamp is: " + stamp);

            System.out.println(Thread.currentThread().getName() + " started");
            atomicStampedReference.compareAndSet(100, 101, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            atomicStampedReference.compareAndSet(101, 100, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + " ended");
        }, "t3").start();

        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();

            System.out.println("First stamp is: " + stamp);
            System.out.println(Thread.currentThread().getName() + " started");
            atomicStampedReference.compareAndSet(100, 2019, stamp, stamp + 1);
            System.out.println(Thread.currentThread().getName() + " ended");
        }, "t4").start();
    }

    public static void main(String[] args) {
        testCAS();
    }
}
