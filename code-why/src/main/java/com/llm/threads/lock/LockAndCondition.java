package com.llm.threads.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockAndCondition {


    static class printSource{

        private ReentrantLock lock = new ReentrantLock();
        private int num = 1;

        Condition condition01 = lock.newCondition();
        Condition condition02 = lock.newCondition();
        Condition condition03 = lock.newCondition();

        public void print5(){
            lock.lock();

            try {
                while (num != 1){
                    condition01.await();
                }
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + "print: "+ i);
                }
                num = 2;
                condition01.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                lock.unlock();
            }

        }

        public void print10(){
            lock.lock();

            try {
                while (num != 2){
                    condition02.await();
                }
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + "print: "+ i);
                }
                num = 3;
                condition02.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                lock.unlock();
            }
        }

        public void print15(){
            lock.lock();

            try {
                while (num != 3){
                    condition03.await();
                }
                for (int i = 0; i < 15; i++) {
                    System.out.println(Thread.currentThread().getName() + "print: "+ i);
                }
                num = 1;
                condition03.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                lock.unlock();
            }
        }

        public static void main(String[] args) {
            printSource printSource = new printSource();
            new Thread(() -> {
                printSource.print5();
            }, "AAA").start();
            new Thread(() -> {
                printSource.print10();
            }, "BBB").start();
            new Thread(() -> {
                printSource.print15();
            }, "CCC").start();

            System.out.println("Main stop");
        }

    }
}
