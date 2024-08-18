package com.llm.threads.threadpool;

import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CommonThreadPool {

    public void threadSample(){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ExecutorService executorService1 = Executors.newCachedThreadPool();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(20);
        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
        ExecutorService executorService3 = Executors.newWorkStealingPool();
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + Instant.now());
        }
    }

    static Callable<String> callableThread = () -> {
        System.out.println(Thread.currentThread().getName() + Instant.now().toString());
        return Thread.currentThread().getName() + Instant.now().toString();
    };


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(new MyThread());
        executorService.submit(callableThread);
        System.out.println("Main");
        executorService.shutdownNow();
    }

}
