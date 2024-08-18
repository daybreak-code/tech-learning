package com.llm.design.factory;

import java.util.HashMap;

public class SmsSender implements Sender{
    @Override
    public void send(String msg) {
        new HashMap<>();
        System.out.println("Send SMS: " + msg);
    }
}
