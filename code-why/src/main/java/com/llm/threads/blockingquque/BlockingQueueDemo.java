package com.llm.threads.blockingquque;

import org.omg.CORBA.TIMEOUT;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueueDemo {

    public static void testBlockingQueue(){
        LinkedBlockingQueue<Object> blockQueue = new LinkedBlockingQueue<>(3);

        try {
            blockQueue.offer(1, 20, TimeUnit.SECONDS);
            blockQueue.offer(1, 20, TimeUnit.SECONDS);
            blockQueue.offer(1, 20, TimeUnit.SECONDS);
            blockQueue.offer(1, 20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        SynchronousQueue<String> synQueue = new SynchronousQueue<>();

        new Thread(() -> {
            try {
                synQueue.put("1");
                System.out.println("1 put");
                synQueue.put("2");
                System.out.println("2 put");
                synQueue.put("3");
                System.out.println("3 put");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                synQueue.take();
                System.out.println("1 get");
                synQueue.take();
                System.out.println("2 get");
                synQueue.take();
                System.out.println("3 get");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
