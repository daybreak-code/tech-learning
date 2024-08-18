package com.llm.seata.tcc.transfer.dao.impl;

import com.llm.seata.tcc.transfer.dao.AccountDao;
import com.llm.seata.tcc.transfer.domains.Account;
import org.mybatis.spring.SqlSessionTemplate;

import java.sql.SQLException;

public class AccountDAOImpl implements AccountDao {

    public SqlSessionTemplate sqlSession;

    public void setSqlSession(SqlSessionTemplate sqlSession){
        this.sqlSession = sqlSession;
    }

    public void addAccount(Account account) throws SQLException {
        sqlSession.insert("addAccount", account);
    }

    public int updateAmount(Account account) throws SQLException {
        return sqlSession.update("updateAmount", account);
    }

    public int updateFreezedAmount(Account account) throws SQLException {
        return sqlSession.update("updateFreezedAmount", account);
    }

    public Account getAccount(String accountNo) throws SQLException {
        return (Account)sqlSession.selectOne("getAccount", accountNo);
    }

    public Account getAccountForUpdate(String accountNo) throws SQLException {
        return (Account)sqlSession.selectOne("getAccountForUpdate", accountNo);
    }

    public void deleteAllAccount() throws SQLException {
        sqlSession.update("deleteAllAccount");
    }
}
