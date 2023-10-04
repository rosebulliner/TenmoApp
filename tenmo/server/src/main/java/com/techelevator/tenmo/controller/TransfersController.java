package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//some of these will have a different path
@PreAuthorize("isAuthenticated()")
@RequestMapping(path="/transfers")
@RestController
public class TransfersController {


    @Autowired
    private TransferDao transferDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountDao accountDao;

//    //this is another ADMIN method that will be useful for testing
    //security concerns, it was useful for creating/testing.
//    @PreAuthorize("hasRole('ADMIN')")
//    @RequestMapping(path="/test", method= RequestMethod.GET)
//    public List<Transfer> listTransfers(){
//        return transferDao.listTransfers();
//    }

    @RequestMapping(method= RequestMethod.GET)
    public List<Transfer> listTransfersForUser(Principal principal){

        return transferDao.getTransfersByUserId(userDao.findIdByUsername(principal.getName()));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Transfer getTransferById(@RequestBody TransferIdDTO id, Principal principal){
        return transferDao.getTransferById(id.getTransferId(), principal.getName());
    }



    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path="/request/{requestFrom}", method=RequestMethod.POST)
    public Transfer requestTransfer(Principal principal, @PathVariable String requestFrom, @RequestBody TransferAmountDTO amount){
        if (!amountValid(amount)){
            return null;
        }

        User loggedInUser = userDao.findByUsername(principal.getName());
        User requestedUser = userDao.findByUsername(requestFrom);

        //if either user doesn't exist, return null.
        if (loggedInUser == null || requestedUser == null){
            return null;
        }

        Transfer pendingTransfer = new Transfer(amount.getAmount(),  requestedUser.getUsername(), loggedInUser.getUsername());
        return transferDao.requestTransfer(pendingTransfer);
    }



    @RequestMapping(path="/pending", method=RequestMethod.GET)
    public List<Transfer> getPendingTransfersForLoggedInUser(Principal principal) {

        return transferDao.listPendingTransfersForLoggedInUser(principal.getName());
    }


    @RequestMapping(path="/deny", method=RequestMethod.POST)
    public Transfer denyTransferById(@RequestBody TransferIdDTO id, Principal principal){
        Transfer transferToDeny = transferDao.getTransferById(id.getTransferId());

        //double check extra make sure this user is allowed to do this!
       if (!transferToDeny.getSenderUsername().equals(principal.getName())){
           return null;
        }
        return transferDao.denyTransfer(transferToDeny);
    }

    @RequestMapping(path="/approve", method=RequestMethod.POST)
    public Transfer approveTransferById(@RequestBody TransferIdDTO id, Principal principal){
        Transfer transferToApprove = transferDao.getTransferById(id.getTransferId());
        if (!transferToApprove.getSenderUsername().equals(principal.getName())){
            return null;
        }

        //right now this just grabs the first account for that person... in the future could make this more custom
        Account sender = accountDao.getAccountsByUserId(userDao.findIdByUsername(transferToApprove.getSenderUsername())).get(0);
        Account recipient = accountDao.getAccountsByUserId(userDao.findIdByUsername(transferToApprove.getRecipientUsername())).get(0);

        //check sender has enough money for transfer, and deduct
        if (!accountDao.deductMoney(sender, transferToApprove.getAmount())){
            return null;
        }
        //add money to recipient
        if (!accountDao.addMoney(recipient, transferToApprove.getAmount())){
            //if something goes wrong with adding the money, put the money back!
            accountDao.addMoney(sender, transferToApprove.getAmount());
            return null;
        }

        return transferDao.acceptTransfer(transferToApprove);

    }




    @RequestMapping(path="/users", method=RequestMethod.GET)
    public List<String> getAvailableRecipients(Principal principal){

        //some chunky logic that removes the prinicipal from the returned list.
        // could move this closer to the DB
        List<String> usersWithoutPrinciple = new ArrayList<>();

        List<String> users = userDao.listAllUserNames();

        for (String username : users){
            if (!username.equals(principal.getName())){
                usersWithoutPrinciple.add(username);
            }
        }
       
        return usersWithoutPrinciple;

    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path="/send/{recipientUserName}", method=RequestMethod.POST)
    public Transfer sendTransfer(@RequestBody TransferAmountDTO amount, @PathVariable String recipientUserName, Principal principal){

        if (!amountValid(amount)){
            return null;
        }

        int senderId = userDao.findIdByUsername(principal.getName());
        int recipientId = userDao.findIdByUsername(recipientUserName);

        //cannot send money to same account
        if (senderId == recipientId){
            return null;
        }

        //just grabbing the first account for each person here... db is set up so that can eventually let both users
        // choose which account to use
        Account sender = accountDao.getAccountsByUserId(senderId).get(0);
        Account recipient = accountDao.getAccountsByUserId(recipientId).get(0);


        //check sender has enough money for transfer, and deduct
        if (!accountDao.deductMoney(sender, amount.getAmount())){
            return null;
        }

        //add money to recipient
        if (!accountDao.addMoney(recipient, amount.getAmount())){
            //if something goes wrong with adding the money, put the money back!
            accountDao.addMoney(sender, amount.getAmount());
            return null;
        };

        //money has already changed hands, creating a log in transfer DB.
        return transferDao.sendTransfer(new Transfer(amount.getAmount(), sender.getAccountId(), recipient.getAccountId(), principal.getName(), recipientUserName));


    }

    private boolean amountValid(TransferAmountDTO amount){
        if (amount.getAmount() > 0){
            return true;
        } else return false;
    }

}
