package com.llm.limit;

//计数器限流算法 Code 实现
public class Case01 {

    //@TODO 如何使用Redis实现分布式集群下的计数器限流算法
    private static volatile int counter = 0;
    private static final int threshold = 100;

    public static boolean tryAcquire(){
        if (counter < threshold){
            counter++;
            return true;
        }
        return false;
    }

    public static boolean tryRelease(){
        if (counter > 0){
            counter--;
            return true;
        }
        return false;
    }
}
