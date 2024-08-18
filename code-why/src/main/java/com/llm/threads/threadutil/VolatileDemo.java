package com.llm.threads.threadutil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class VolatileDemo {

    static class Data {
        volatile int num;

        AtomicInteger atomicNum = new AtomicInteger(0);

        public void addT60(){
            this.num = 60;
        }

        public void addNum(){
            this.num++;
        }

        public synchronized void addAtomicNum(){
            this.atomicNum.addAndGet(1);
        }
    }

    public static void testVolatileNotAtomic(){
        Data data = new Data();
        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " start");
                for (int j = 0; j < 1000; j++) {
                    data.addNum();
                    data.addAtomicNum();
                }
                System.out.println(Thread.currentThread().getName() + " end");
                countDownLatch.countDown();
            }, String.valueOf(i)).start();

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("int num: "+ data.num);
        System.out.println("atomic int num: "+ data.atomicNum.get());
        System.out.println("Main stop");
    }

    public static void testVolatileCouldRead(){

        Data data = new Data();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+" num is: "+ data.num);

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            data.addT60();
            System.out.println(Thread.currentThread().getName()+" num is: "+ data.num);
        }, "AAA").start();

        while (data.num == 0){

        }
        System.out.println("mission is over");
    }

    public static void main(String[] args) {
        testVolatileNotAtomic();
    }

}
