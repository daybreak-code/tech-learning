package com.llm.redis.lock.service;

import com.llm.redis.lock.entity.CustomerBalance;

import java.util.concurrent.Callable;

public interface RedisService {

    <T> T callWithLock(CustomerBalance customerBalance, Callable<T> callable) throws Exception;
}
