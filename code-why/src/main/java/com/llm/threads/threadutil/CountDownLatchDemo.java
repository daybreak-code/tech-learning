package com.llm.threads.threadutil;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    public static void main(String[] args) {
        testCountDownLunch();
    }


    public static void testCountDownLunch(){
        CountDownLatch countDownLunch = new CountDownLatch(6);
        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " Start to count down");
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                countDownLunch.countDown();
            }, String.valueOf(i)).start();
        }
        try {
            countDownLunch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Main");
    }

}
