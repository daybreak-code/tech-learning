package com.llm.zookeeper.config;

import lombok.Data;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@Data
public class ZkConfiguration {

    @Resource
    private CuratorConfig curatorConfig;

    //建立连接的思路：重试次数，睡眠次数
    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework(){
        RetryPolicy retryPolicy = new RetryNTimes(curatorConfig.getRetryCount(), curatorConfig.getSleepBetweenRetries());
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(
                curatorConfig.getConnect(),
                curatorConfig.getSessionTimeout(),
                curatorConfig.getConnectionTimeout(), retryPolicy
        );
        return curatorFramework;
    }
}
