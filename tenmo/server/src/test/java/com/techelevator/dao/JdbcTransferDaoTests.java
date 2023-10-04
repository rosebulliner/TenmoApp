package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransfersDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTransferDaoTests extends BaseDaoTests{


    JdbcTransfersDao sut;

    //this should automatically connect to the database built by test-data.sql...
    // from my understanding the dataSource is connected to the the main sql file and not the test-data?
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransfersDao(jdbcTemplate);
    }


    //test list all transfers


    //test get transfer by id (passing username too, should only return trasnfers associated with that user)
    @Test
    public void getTransferById_Int_String_returns_correct_transfer(){
        Assert.assertEquals(1004 ,sut.getTransferById(3003,"Mr_Peanutbutter").getSenderId());
        Assert.assertEquals(1003, sut.getTransferById(3003, "DianeNguyen").getRecipientId());
    }
   // test get transfer by id // not sure if use this method in cli so im not going to remove it, but i dont see use for it in server.

    //test list all transfers for logged-in user (just pass the user_id, DAO doesn't know who logged in user is)
    //sercurity issues? that method was good for testing but since dont have an admin I dont think should include it.

    //test request transfer, doesn't handle taking/giving money, just creates trasnfer
    @Test
    public void requestTransfer_creates_new_transfer(){
        Transfer transfer = new Transfer(300, 1006, 1007, "bob", "user");
        Assert.assertNotNull(sut.requestTransfer(transfer));
        Assert.assertEquals(300,transfer.getAmount(),.001);
        Assert.assertEquals("Approved",transfer.getStatus());
    }

    //test sendTransfer, see above
            // the same as request transfer?
    @Test
    public void sendTransfer_creates_new_transfer(){
        Transfer transfer = new Transfer(300, 1006, 1007, "bob", "user");
        Assert.assertNotNull(sut.sendTransfer(transfer));
        Assert.assertEquals(300,transfer.getAmount(),.001);
        Assert.assertEquals("Approved",transfer.getStatus());
    }

    //test approve / deny transfer
    @Test
    public void acceptTransfer_approves_transfer(){
        Transfer transfer= sut.getTransferById(3002);
        Assert.assertEquals("Pending", transfer.getStatus());
        Assert.assertEquals("Approved", sut.acceptTransfer(transfer).getStatus());


    }
    @Test
    public void denyTransfer_rejects_transfer(){
            Transfer transfer= sut.getTransferById(3004);
            Assert.assertEquals("Pending", transfer.getStatus());
           // Assert.assertEquals("Rejected", sut.acceptTransfer(transfer).getStatus());

        }

    //these should validate whether the transfer was in "pending", make sure non pending transfers can't be approved

    //test list usernames



}
