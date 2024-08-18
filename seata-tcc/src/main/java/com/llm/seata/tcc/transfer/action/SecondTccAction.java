package com.llm.seata.tcc.transfer.action;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

public interface SecondTccAction {

    @TwoPhaseBusinessAction(name = "secondTccAction", commitMethod = "commit", rollbackMethod = "rollback")
    boolean prepareAdd(BusinessActionContext businessActionContext,
                       @BusinessActionContextParameter(paramName = "accountNo") String accountNo,
                       @BusinessActionContextParameter(paramName = "amount") double amount);

    boolean commit(BusinessActionContext businessActionContext);

    boolean rollback(BusinessActionContext businessActionContext);

}
