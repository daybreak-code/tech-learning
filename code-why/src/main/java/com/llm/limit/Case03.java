package com.llm.limit;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

// 滑动窗口限流算法 Code 实现
public class Case03 {

    private int SUB_CYCLE = 10;
    private int thresholdPerMin = 100;
    private final TreeMap<Long, Integer> counters = new TreeMap<Long, Integer>();

    public synchronized  boolean slidingWindowsTryAcquire(){
        long currentWindowTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) / SUB_CYCLE * SUB_CYCLE;
        int currentWindowNum = countCurrentWindow(currentWindowTime);

        if (currentWindowNum >= thresholdPerMin) {
            return false;
        }
        Integer integer = counters.get(currentWindowTime);
        integer++;
        counters.put(currentWindowTime, integer);
        return true;
    }

    private synchronized int countCurrentWindow(long currentWindowTime) {
        long startTime = currentWindowTime - SUB_CYCLE * (60 / SUB_CYCLE - 1);
        int count = 0;
        Iterator<Map.Entry<Long, Integer>> iterator = counters.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Long, Integer> entry = iterator.next();
            if (entry.getKey() < startTime){
                iterator.remove();
            } else {
                count = count + entry.getValue();
            }
        }
        return count;
    }
}
