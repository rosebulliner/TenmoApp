package com.techelevator.services;

import com.techelevator.model.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;



public class AccountServiceTest {

    private AccountService sut = new AccountService();

    @Before
    public void setup(){
        sut.setAuthToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZWZmIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY4MjY5MTk4OH0.iVPjnHcdQ8Fo376ZaMlF3ok9H2v1KEsWGdoGjVxC2tVbVR6-TcCByOkBGgV1VA9tHeXIk6gwrPB5XiMwV9PS2Q");
    }


    @Test
    public void getAccountsForLoggedInUser_Returns_Something(){

        Account[] test = sut.getAccountsForLoggedInUser();
        for (Account account : test){
            System.out.println(account.getAccountId());
        }
        Assert.assertNotNull(test);

    }

    @Test
    public void createAccountForLoggedInUser_Creates_Account_And_Returns_List_Of_Accounts_Including_Created_Account(){

        Account[] test = sut.getAccountsForLoggedInUser();
        int expected = test.length + 1;
        Account[] actual = sut.createNewAccountForLoggedInUser();

        for (Account account : actual){
            System.out.println(account.getAccountId());
        }

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual.length);

    }





}
