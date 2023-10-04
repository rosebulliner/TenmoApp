package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface TransferDao {

    //ADMIN METHOD
//    @PreAuthorize("hasRole('ADMIN')")
//    List<Transfer> listTransfers();


    //get transfer by id
    Transfer getTransferById(int transferId, String username);

    Transfer getTransferById(int transferId);


    //get all transfers for account_id
    List<Transfer>getTransfersByAccountId(int accountId);

    //get all transfers for user_id
    List<Transfer>getTransfersByUserId(int userId);



    //create a new transfer
    Transfer sendTransfer(Transfer transfer);


    //requestTransfer
    Transfer requestTransfer(Transfer transfer);


    //acceptTransfer
    Transfer acceptTransfer(Transfer transfer);

    //denyTransfer
    Transfer denyTransfer(Transfer transfer);

    //get all pending transfers
    List<Transfer>listPendingTransfersForLoggedInUser(String username);


}
