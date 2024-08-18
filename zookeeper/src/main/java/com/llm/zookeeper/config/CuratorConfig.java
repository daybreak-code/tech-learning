package com.llm.zookeeper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "curator")
public class CuratorConfig {

    private int retryCount;

    private int sleepBetweenRetries;

    private String connect;

    private int sessionTimeout;

    private int connectionTimeout;

}
