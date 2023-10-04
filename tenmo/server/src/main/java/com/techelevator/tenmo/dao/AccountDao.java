package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {

    //ADMIN ONLY METHOD, LIST ACCOUNTS:
    List<Account> listAccounts();

    //get account by account id
    Account getAccountById(int accountId);

    //get account(s) by user_id
    List<Account>getAccountsByUserId(int userId);

    //create a new account
    Account createAccount(Account newAccount);

    //create an account by user_id
    Account createAccountByUserId (int user_id);

    //add money to account balance
    boolean addMoney(Account account, double amount);

    //check balance and THEN deduct an amount of money
    boolean deductMoney(Account account, double amount);


}
