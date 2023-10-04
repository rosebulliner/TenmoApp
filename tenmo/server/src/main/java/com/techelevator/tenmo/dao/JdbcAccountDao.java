package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Account getAccountById(int accountId) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
            if (result.next()){
                account = mapToAccount(result);
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        }
        return account;
    }

    @Override
    public List<Account> getAccountsByUserId(int userId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ? " +
                "ORDER BY account_id;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
            while (result.next()){
                accounts.add(mapToAccount(result));
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        }


        return accounts;
    }

    @Override
    public Account createAccount(Account newAccount) {
        Account createdAccount = null;
        String sql = "INSERT INTO account (user_id, balance) " +
                "VALUES (?, ?) RETURNING account_id;";
        try {
            int accountId = jdbcTemplate.queryForObject(sql, int.class, newAccount.getUserId(), newAccount.getBalance());

        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation", e);
        }

        return createdAccount;
    }

    @Override
    public Account createAccountByUserId(int userId){
        Account createdAccount = null;
        String sql = "INSERT INTO account (user_id) " +
                "VALUES (?) RETURNING account_id;";
        try {
            int accountId = jdbcTemplate.queryForObject(sql, int.class, userId);
            createdAccount = getAccountById(accountId);

        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation", e);
        }

        return createdAccount;


    }

    @Override
    public boolean addMoney(Account account, double amount) {
        boolean success = false;
        String checkBalance = "SELECT balance FROM account WHERE account_id = ?;";
        String addAmount = "UPDATE account SET balance = ? WHERE account_id = ?;";

        try {
            SqlRowSet checkBalanceResult = jdbcTemplate.queryForRowSet(checkBalance, account.getAccountId());
            if (checkBalanceResult.next()) {
                double balance = checkBalanceResult.getDouble("balance");

                //this check could be redundant/removed later, but for now, it's an added safety feature
                if (account.getBalance() != balance) {
                    throw new DaoException("Balance in account object DOES NOT match db record!");
                }

                double newBalance = balance + amount;

                //update the account object that got passed in...
                account.setBalance(newBalance);
                //update the account in db
                jdbcTemplate.update(addAmount, newBalance, account.getAccountId());
                success = true;
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation", e);
        }


        return success;
    }

    @Override
    public boolean deductMoney(Account account, double amount) {
        boolean success = false;
        String checkBalance = "SELECT balance FROM account WHERE account_id = ?;";
        String deductAmount = "UPDATE account SET balance = ? WHERE account_id =?;";

        try {
            SqlRowSet checkBalanceResult = jdbcTemplate.queryForRowSet(checkBalance, account.getAccountId());
            if (checkBalanceResult.next()) {
                double balance = checkBalanceResult.getDouble("balance");
                //this check could be redundant/removed later, but for now, it's an added safety feature
                if (account.getBalance() != balance) {
                    throw new DaoException("Balance in account object DOES NOT match db record!");
                }
                double newBalance = balance - amount;

                //check thattransaction is allowed...
                if (newBalance < 0) {
                    return false;
                }

                //update the account object that got passed in...
                account.setBalance(newBalance);
                //update the account indb
                jdbcTemplate.update(deductAmount, newBalance, account.getAccountId());
                success = true;
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation", e);
        }


        return success;
    }


    public List<Account> listAccounts(){

        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance FROM account;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
            while (result.next()){
                accounts.add(mapToAccount(result));
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        }

        return accounts;
    }



    private Account mapToAccount(SqlRowSet result){
        Account account = new Account();

        account.setAccountId(result.getInt("account_id"));
        account.setBalance(result.getDouble("balance"));
        account.setUserId(result.getInt("user_id"));

        return account;
    }


}
