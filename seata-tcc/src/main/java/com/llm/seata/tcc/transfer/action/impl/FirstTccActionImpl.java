package com.llm.seata.tcc.transfer.action.impl;

import com.llm.seata.tcc.transfer.action.FirstTccAction;
import com.llm.seata.tcc.transfer.dao.AccountDao;
import com.llm.seata.tcc.transfer.domains.Account;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

public class FirstTccActionImpl implements FirstTccAction {

    /**
     * 扣钱账户 DAO
     */
    private AccountDao fromAccountDAO;

    /**
     * 扣钱数据源事务模板
     */
    private TransactionTemplate fromDsTransactionTemplate;


    public boolean prepareMinus(BusinessActionContext businessActionContext, final String accountNo, final double amount) {
        final String xid = businessActionContext.getXid();
        return fromDsTransactionTemplate.execute((status) -> {
            try {
                Account account = fromAccountDAO.getAccountForUpdate(accountNo);
                if (null == account) {
                    throw new RuntimeException("Account don't exists");
                }
                if (account.getAmount() - amount < 0) {
                    throw new RuntimeException("balance isn't finished");
                }
                //freeze
                double freezedAmount = account.getFreezedAmount() + amount;
                account.setFreezedAmount(freezedAmount);
                fromAccountDAO.updateFreezedAmount(account);
                System.out.println(String.format("prepareMinus account[%s] amount[%f], dtx transaction id: %s.",
                        accountNo, amount, xid));
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }

    public boolean commit(BusinessActionContext businessActionContext) {
        final String xid = businessActionContext.getXid();
        final String accountNo = String.valueOf(businessActionContext.getActionContext("accountNo"));
        final double amount = Double.valueOf(String.valueOf(businessActionContext.getActionContext("amount")));
        return fromDsTransactionTemplate.execute((status) -> {
            try {
                Account account = fromAccountDAO.getAccountForUpdate(accountNo);
                double newAmount = account.getAmount() - amount;
                if (newAmount < 0) {
                    throw new RuntimeException("balance is not finished");
                }
                account.setAmount(newAmount);
                account.setFreezedAmount(account.getFreezedAmount() - amount);
                fromAccountDAO.updateAmount(account);
                System.out.println(
                        String.format("minus account[%s] amount[%f], dtx transaction id: %s.", accountNo, amount, xid));
                return true;
            } catch (Throwable t) {
                t.printStackTrace();
                status.setRollbackOnly();
                return false;
            }

        });
    }

    public boolean rollback(BusinessActionContext businessActionContext) {
        //分布式事务ID
        final String xid = businessActionContext.getXid();
        final String accountNo = String.valueOf(businessActionContext.getActionContext("accountNo"));
        final double amount = Double.valueOf(String.valueOf(businessActionContext.getActionContext("accountNo")));
        return fromDsTransactionTemplate.execute((status) -> {
            try {
                Account account = fromAccountDAO.getAccountForUpdate(accountNo);
                if (account == null) {
                    return true;
                }
                account.setFreezedAmount(account.getFreezedAmount() - amount);
                fromAccountDAO.updateFreezedAmount(account);
                System.out.println(String
                        .format("Undo prepareMinus account[%s] amount[%f], dtx transaction id: %s.", accountNo, amount,
                                xid));
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public void setFromAccountDAO(AccountDao fromAccountDAO) {
        this.fromAccountDAO = fromAccountDAO;
    }

    public void setFromDsTransactionTemplate(TransactionTemplate fromDsTransactionTemplate) {
        this.fromDsTransactionTemplate = fromDsTransactionTemplate;
    }
}
