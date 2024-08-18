package com.llm.design.factory;

public class EmailFactory extends Producer{

    @Override
    Sender produce() {
        return new EmailSender();
    }
}
