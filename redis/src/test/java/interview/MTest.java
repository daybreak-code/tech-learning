package interview;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MTest {

    // A -> B -> C -> D
    // B.next -> A
    //

    public static Node reverseList(Node root){
        if (Objects.isNull(root)){
            return root;
        }
        if (root.next != null){
            root.next = reverseList(root.next);
        }
        return null;
    }

    ReentrantLock lock = new ReentrantLock();
    int val = 1;

    static Runnable print1 = () -> {

        lock.lock();

        try {
            while (val != 1){
               Thread.sleep();
            }
            val = 2;
            System.out.println(1);
            this.notifyAll();
        } finally {
            lock.unlock();
        }
    };

    static Runnable print2 = () -> {
        lock.lock();

        try {
            while (val != 2){
                Thread.sleep(10);
            }
            val = 3;
            System.out.println(2);
            this.notifyAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    };

    static Runnable print3 = () -> {
        lock.lock();

        try {
            while (val != 3){
                Thread.sleep(10);
            }
            System.out.println(3);
            this.notifyAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    };

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                3, 4, 10L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20)
        );

        threadPoolExecutor.execute(print1);
        threadPoolExecutor.execute(print2);
        threadPoolExecutor.execute(print3);
    }

    static class Node {
        int val;
        Node next;
    }
}
