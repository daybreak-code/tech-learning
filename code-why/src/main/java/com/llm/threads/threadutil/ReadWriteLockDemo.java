package com.llm.threads.threadutil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {

    public static volatile Map<String, Object> map = new HashMap<>();

    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();


    public void put(String key, Object val){
        readWriteLock.writeLock().lock();
        try {
            System.out.println("Start to insert");
            TimeUnit.SECONDS.sleep(3);
            map.put(key, val);
            System.out.println("End to insert");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public void get(String key){
        readWriteLock.readLock().lock();
        try {
            System.out.println("Start to get");
            TimeUnit.SECONDS.sleep(3);
            Object o = map.get(key);
            System.out.println("Got value: "+ o);
            System.out.println("End to get");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void clearMap(){
        map.clear();
    }

    public static void main(String[] args) {
        ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                readWriteLockDemo.put(finalI + "", finalI);
            }).start();
        }

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                readWriteLockDemo.get(finalI + "");
            }).start();
        }

        System.out.println("Main");
    }

}
