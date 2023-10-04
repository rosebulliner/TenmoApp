package com.techelevator.services;

import com.techelevator.model.Account;
import com.techelevator.model.Transfer;
import com.techelevator.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TransferServiceTest {

    private TransferService sut = new TransferService();

    @Before
    public void setup(){
        sut.setAuthToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZWZmIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY4MjY5MTk4OH0.iVPjnHcdQ8Fo376ZaMlF3ok9H2v1KEsWGdoGjVxC2tVbVR6-TcCByOkBGgV1VA9tHeXIk6gwrPB5XiMwV9PS2Q");
    }


    @Test
    public void getAccountsForLoggedInUser_Returns_Something(){

        String[] test = sut.getAvailableUsers();

        for (String user : test){
            System.out.println(user);
        }
        Assert.assertNotNull(test);

    }

    @Test
    public void sendTransfer_returns_transfer(){

        Transfer test = sut.sendTransfer("hank", 300);
        System.out.println(test);


        Assert.assertNotNull(test);
    }

    @Test
    public void requestTransfer_returns_transfer(){

        Transfer test = sut.requestTransfer("hank", 300);
        System.out.println(test);

        Assert.assertNotNull(test);
    }



}
