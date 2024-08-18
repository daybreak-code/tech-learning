package com.llm.zookeeper.config;

import org.example.component.TestComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class CuratorConfigTest {

    @Autowired
    TestComponent testComponent;

    @Test
    void setUp() {
        testComponent.printVal();
    }
}