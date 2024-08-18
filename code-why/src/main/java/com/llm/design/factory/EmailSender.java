package com.llm.design.factory;

public class EmailSender implements Sender{
    @Override
    public void send(String msg) {
        System.out.println("Send email: " + msg);
    }
}
