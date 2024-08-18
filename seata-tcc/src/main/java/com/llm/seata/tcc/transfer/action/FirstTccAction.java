package com.llm.seata.tcc.transfer.action;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;

//TCC参与者： 扣钱
public interface FirstTccAction {

    boolean prepareMinus(BusinessActionContext businessActionContext,
                         @BusinessActionContextParameter(paramName = "accountNo") String accountNo,
                         @BusinessActionContextParameter(paramName = "amount") double amount);

    boolean commit(BusinessActionContext businessActionContext);

    boolean rollback(BusinessActionContext businessActionContext);

}
