package com.techelevator.model;

public class TransferIdDTO {
    private int transferId;


    public TransferIdDTO(int transferId) {
        this.transferId = transferId;
    }

    public TransferIdDTO() {
    }


    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }
}
