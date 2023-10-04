package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path="/accounts")
public class AccountController {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UserDao userDao;

    //ADMIN ONLY METHOD, LIST ALL ACCOUNTS (useful for testing)

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path="/test", method=RequestMethod.GET)
    public List<Account> listAccounts(){
        return accountDao.listAccounts();
    }




    //get account by accountId

    //need to re-do this:   can't use this route / get for this.
    //probably need to do this as a POST request, passing the id that way.
    @RequestMapping(path="/{id}", method=RequestMethod.GET)
    public Account getAccountById(@PathVariable int id){
        return accountDao.getAccountById(id);
    }

    //get balance(s) by userId
    //in  finished product, this route will /accounts and the userID parameter will come from the Principal object
    @RequestMapping(method=RequestMethod.GET)
    public List<Account> getAccountsForLoggedInUser(Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return accountDao.getAccountsByUserId(userId);
    }

    @RequestMapping(path="/balance", method=RequestMethod.GET)
    public Map<String, Double> getBalanceForLoggedInUser(Principal principal){
        Map<String, Double>balances = new HashMap<>();
        int userId = userDao.findIdByUsername(principal.getName());
        List<Account>accounts = accountDao.getAccountsByUserId(userId);

        if (accounts.size() < 1){
            return null;
        }

        if (accounts.size() < 2){
            balances.put("Balance",accounts.get(0).getBalance());
            return balances;
        }

        for (Account account : accounts){
                balances.put(String.valueOf("Account " + account.getAccountId()) + " Balance", account.getBalance());

        }

        return balances;
    }


    //create account
    //reminder: there are two methods to use for this...  should delete one once app is fully functioning

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method=RequestMethod.POST)
    public Account createAccount(Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return accountDao.createAccountByUserId(userId);
    }

}
