package com.llm.design.factory;

public abstract class Producer {

    Producer defaultProducer = new SmsFactory();

    abstract Sender produce();

    public Sender produce(String type){
        if ("mail".equals(type)){
            return new EmailFactory().produce();
        } else if ("sms".equals(type)) {
            return new SmsFactory().produce();
        } else {
            return defaultProducer.produce();
        }
    }

}
