package com.llm.seata.tcc.transfer.action.impl;


import org.example.configuration.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class FirstTccActionImplTest {

    @Autowired
    TestConfiguration testConfiguration;

    @Test
    public void setFromAccountDAO() {
        System.out.println(testConfiguration);
    }
}