package com.llm.seata.tcc.transfer.dao;

import com.llm.seata.tcc.transfer.domains.Account;

import java.sql.SQLException;

public interface AccountDao {
    void addAccount(Account account) throws SQLException;

    int updateAmount(Account account) throws SQLException;

    int updateFreezedAmount(Account account) throws SQLException;

    Account getAccount(String accountNo) throws SQLException;

    Account getAccountForUpdate(String accountNo) throws SQLException;

    void deleteAllAccount() throws SQLException;
}
