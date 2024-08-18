package com.llm.design.factory;

public class SmsFactory extends Producer{
    @Override
    Sender produce() {
        return new SmsSender();
    }
}
