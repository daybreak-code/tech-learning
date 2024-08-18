package com.llm.redis.lock.business;

import com.llm.redis.lock.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    @Autowired
    RedisService redisService;

//    public int updateById(CustomerBalance customerBalance) throws Exception {
//        return redisService.callWithLock(customerBalance, ()-> customerBalanceMapper.updateById(customerBalance));
//    }

}
