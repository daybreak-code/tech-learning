package com.llm.limit;

//令牌桶限流算法 代码实现
public class Case05 {

    private final int capacity;
    private final int rate;
    private int tokens;
    private long lastRefillTimestamp;

    public Case05(int capacity, int rate){
        this.capacity = capacity;
        this.rate = rate;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest(){
        refill();
        if (tokens > 0){
            tokens--;
            return true;
        } else {
            return false;
        }
    }

    private void refill(){
        long now = System.currentTimeMillis();
        if(now > lastRefillTimestamp){
            int generatedTokens = (int) ((now - lastRefillTimestamp) / 1000 * rate);
            tokens = Math.min(tokens + generatedTokens, capacity);
            lastRefillTimestamp = now;
        }
    }
}
