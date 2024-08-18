package com.llm.seata.tcc.transfer.action.impl;

import com.llm.seata.tcc.transfer.action.SecondTccAction;
import com.llm.seata.tcc.transfer.dao.AccountDao;
import com.llm.seata.tcc.transfer.domains.Account;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

public class SecondTccActionImpl implements SecondTccAction {

    /**
     * 加钱账户 DAP
     */
    private AccountDao toAccountDAO;

    private TransactionTemplate toDsTransactionTemplate;


    @Override
    public boolean prepareAdd(BusinessActionContext businessActionContext, String accountNo, double amount) {
        final String xid = businessActionContext.getXid();
        return toDsTransactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus transactionStatus) {
                try {
                    Account account = toAccountDAO.getAccountForUpdate(accountNo);
                    if (null == account){
                        System.out.println(
                                "prepareAdd: 账户[" + accountNo + "]不存在, txId:" + businessActionContext.getXid());
                        return false;
                    }
                    double freezedAmount = account.getFreezedAmount() + amount;
                    account.setFreezedAmount(freezedAmount);
                    toAccountDAO.updateFreezedAmount(account);
                    System.out.println(String
                            .format("prepareAdd account[%s] amount[%f], dtx transaction id: %s.", accountNo, amount, xid));
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
    }

    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        final String xid = businessActionContext.getXid();
        final String accountNo = String.valueOf(businessActionContext.getActionContext("accountNo"));
        final double amount = Double.valueOf(String.valueOf(businessActionContext.getActionContext("amount")));
        return toDsTransactionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    Account account = toAccountDAO.getAccountForUpdate(accountNo);
                    //加钱
                    double newAmount = account.getAmount() + amount;
                    account.setAmount(newAmount);
                    //冻结金额 清除
                    account.setFreezedAmount(account.getFreezedAmount() - amount);
                    toAccountDAO.updateAmount(account);

                    System.out.println(
                            String.format("add account[%s] amount[%f], dtx transaction id: %s.", accountNo, amount, xid));
                    return true;
                } catch (Throwable t) {
                    t.printStackTrace();
                    status.setRollbackOnly();
                    return false;
                }
            }
        });

    }

    @Override
    public boolean rollback(BusinessActionContext businessActionContext) {
        //分布式事务ID
        final String xid = businessActionContext.getXid();
        //账户ID
        final String accountNo = String.valueOf(businessActionContext.getActionContext("accountNo"));
        //转出金额
        final double amount = Double.valueOf(String.valueOf(businessActionContext.getActionContext("amount")));
        return toDsTransactionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    Account account = toAccountDAO.getAccountForUpdate(accountNo);
                    if (account == null) {
                        //账户不存在, 无需回滚动作
                        return true;
                    }
                    //冻结金额 清除
                    account.setFreezedAmount(account.getFreezedAmount() - amount);
                    toAccountDAO.updateFreezedAmount(account);

                    System.out.println(String
                            .format("Undo prepareAdd account[%s] amount[%f], dtx transaction id: %s.", accountNo, amount,
                                    xid));
                    return true;
                } catch (Throwable t) {
                    t.printStackTrace();
                    status.setRollbackOnly();
                    return false;
                }
            }
        });
    }
}
