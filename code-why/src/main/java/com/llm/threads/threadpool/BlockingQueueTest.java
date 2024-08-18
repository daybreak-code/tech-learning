package com.llm.threads.threadpool;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueueTest {

    public static final SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>();

    public static void main(String[] args) {
        new Thread(() -> {
            while (true){
                try {
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("Put Start");
                    synchronousQueue.put(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("put end");
            }
        }).start();
        new Thread(() -> {
            while (true){
                try {
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("Take Start");
                    synchronousQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Take end");
            }
        }).start();
    }

}
