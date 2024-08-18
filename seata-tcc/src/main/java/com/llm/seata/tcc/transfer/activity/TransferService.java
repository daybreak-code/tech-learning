package com.llm.seata.tcc.transfer.activity;

public interface TransferService {
    boolean transfer(String from, String to, double amount);

}
