package com.llm.threads.lock;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerAndConsumer {

    static class Data {

        private int num = 0;

        public synchronized void increment() throws InterruptedException {
            System.out.println("increment");
            if (num != 0){
                this.wait();
            }
            this.num++;
            System.out.println(Thread.currentThread().getName() + "num is: "+ num);
            this.notifyAll();

        }

        public synchronized void decrement() throws InterruptedException {
            System.out.println("decrement");
            if (num <= 0){
                this.wait();
            }
            this.num--;
            System.out.println(Thread.currentThread().getName() + "num is: "+ num);

            this.notifyAll();
        }

    }

    public static void traditionalMutipleThread() throws InterruptedException {
        Data data = new Data();

        new Thread(() -> {
            try {
                data.increment();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                data.decrement();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                data.increment();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                data.decrement();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Thread.sleep(5);

        System.out.println("Main Stop");
    }

    public static void main(String[] args) throws InterruptedException {
        DataBlockingQueue mySource = new DataBlockingQueue(new LinkedBlockingQueue<>(10));

        new Thread(() -> {
            try {
                mySource.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"AAA").start();

        new Thread(() -> {
            try {
                mySource.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"BBB").start();

        TimeUnit.SECONDS.sleep(5);

        System.out.println("5秒已经过了，开始结束吧");

        mySource.stopProduce();
    }

    static class DataBlockingQueue{

        private AtomicInteger data = new AtomicInteger();
        private BlockingQueue<String> queue = null;
        private volatile boolean prodFlag = true;
        private volatile boolean consumeFlag = true;

        public DataBlockingQueue(LinkedBlockingQueue<String> queue) {
            this.queue = queue;
            System.out.println("Set Data Blocking Queue");
        }

        public void produce() throws InterruptedException {
            System.out.println("produce");
            while (prodFlag){
                String val = data.incrementAndGet() + "";
                boolean result = queue.offer(val, 2, TimeUnit.SECONDS);
                if (result){
                    System.out.println("插入队列成功");
                } else {
                    System.out.println("插入失败");
                }
                TimeUnit.SECONDS.sleep(2);
            }
            System.out.println("大老板叫停生产");
        }

        public void consume() throws InterruptedException {
            System.out.println("consume");
            while (consumeFlag){
                String poll = queue.poll(2, TimeUnit.SECONDS);
                System.out.println("Got data from poll");
                if (poll == null || "".equalsIgnoreCase(poll)){
                    consumeFlag = false;
                    System.out.println("超过两秒钟没有渠道，消费退出");
                } else {
                    System.out.println("消费成功");
                }
            }
        }

        public void stopProduce(){
            this.prodFlag = false;
        }
    }


}
