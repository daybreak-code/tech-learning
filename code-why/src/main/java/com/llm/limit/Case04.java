package com.llm.limit;

//漏桶限流算法 代码实现
public class Case04 {

    private final long capacity; //桶的容量
    private final long rate; //漏桶出水速率
    private long water; //当前桶中的水量
    private long lastLeakTimestamp; //上次漏水时间戳

    public Case04(long capacity, long rate) {
        this.capacity = capacity;
        this.rate = rate;
        this.water = 0;
        this.lastLeakTimestamp = System.currentTimeMillis();
    }

    public synchronized boolean tryConsume(long waterRequested){
        leak();
        if (water + waterRequested <= capacity){
            water += waterRequested;
            return true;
        } else {
            return false;
        }
    }

    private void leak(){
        long now = System.currentTimeMillis();
        long elapsedTime = now - lastLeakTimestamp;
        long leakedWater = elapsedTime * rate / 1000;
        if (leakedWater > 0){
            water = Math.max(0, water - leakedWater);
            lastLeakTimestamp = now;
        }
    }
}
