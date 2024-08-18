package com.llm.redis.lock.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerBalance {

    private String customerNumber;
    private String subAccountNumber;
    private String currencyCode;

}
