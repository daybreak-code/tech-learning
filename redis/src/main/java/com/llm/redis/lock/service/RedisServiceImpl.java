package com.llm.redis.lock.service;

import com.llm.redis.lock.entity.CustomerBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisServiceImpl implements RedisService{


    private final static int DEFAULT_LOCK_EXPIRE_TIME = 20;
    private final static String LOCK_PREFIX = "LOCK:CUSTOMER_BALANCE";


    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public <T> T callWithLock(CustomerBalance customerBalance, Callable<T> callable) throws Exception {
        //自定义lock key
        String lockKey = getLockKey(customerBalance.getCustomerNumber(), customerBalance.getSubAccountNumber(), customerBalance.getCurrencyCode());
        //将UUID当做value，确保唯一性
        String lockReference = UUID.randomUUID().toString();

        try {
            if (!lock(lockKey, lockReference, DEFAULT_LOCK_EXPIRE_TIME, TimeUnit.SECONDS)) {
                throw new Exception("lock加锁失败");
            }
            return callable.call();
        } finally {
            unlock(lockKey, lockReference);
        }
    }

    String getLockKey(String customerNumber, String subAccountNumber, String currentCode){
        return String.format("%s:%s:%s:%s", LOCK_PREFIX, customerNumber, subAccountNumber, currentCode);
    }

    private boolean lock(String key, String value, long timeout, TimeUnit timeUnit){
        Boolean locked;
        try {
            //SET_IF_ABSENT --> NX: Only set the key if it does not already exist.
            //SET_IF_PRESENT --> XX: Only set the key if it already exist.
            locked = (Boolean) redisTemplate.execute(
                    (RedisCallback<Boolean>) connection ->
                    connection.set(
                            key.getBytes(StandardCharsets.UTF_8),
                            value.getBytes(StandardCharsets.UTF_8),
                            Expiration.from(timeout, timeUnit),
                            RedisStringCommands.SetOption.SET_IF_ABSENT));
        } catch (Exception e) {
            log.error("Lock failed for redis key: {}, value: {}", key, value);
            locked = false;
        }
        return locked != null && locked;
    }

    private boolean unlock(String key, String value){
        Boolean locked;
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] " +
                    "then return redis.call('del', KEYS[1]) " +
                    "else return 0 end";
            Boolean unlockState = (Boolean) redisTemplate.execute(
                    (RedisCallback<Boolean>) connection ->
                    connection.eval(script.getBytes(),
                            ReturnType.BOOLEAN,
                            1,
                            key.getBytes(StandardCharsets.UTF_8),
                            value.getBytes(StandardCharsets.UTF_8)));
            return unlockState == null || !unlockState;
        } catch (Exception e) {
            System.out.println("unlock failed for key {"+key+"} and value { " + value+ "+}");
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {


            List<String> list = new ArrayList<>();
            Integer[] arr ={3,2,1};
            Arrays.sort(arr, Collections.reverseOrder());
        Arrays.stream(arr).forEach(System.out::println);

    }

}
