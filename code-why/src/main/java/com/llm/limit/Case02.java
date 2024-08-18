package com.llm.limit;

//固定窗口限流算法 Code 实现
public class Case02 {

    public static Integer counter = 0; //统计请求数
    public static long lastAcquireTime = 0L;
    public static final Long windowUnit = 1000l; //假设固定时间窗口是1000ms
    public static final Integer threshold = 10; //窗口阈值是10

    public static boolean tryAcquire(){
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAcquireTime > windowUnit){
            counter = 0;
            lastAcquireTime = currentTime;
        }
        if (counter < threshold){
            counter++;
            return true;
        }
        return false;
    }

}
