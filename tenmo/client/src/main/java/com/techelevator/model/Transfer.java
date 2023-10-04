package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

public class Transfer {

    private int transferId;
    private double amount;
    private int senderId;
    private int recipientId;
    @JsonIgnoreProperties
    private LocalDateTime dateTimeRequested;
    @JsonIgnoreProperties
    private LocalDateTime dateTimeExecuted;
    private String status = "Approved";
    private String senderUsername;
    private String recipientUsername;


    public Transfer() {
    }

    public Transfer(int transferId, double amount, int senderId, int recipientId, LocalDateTime dateTimeRequested, String status) {
        this.transferId = transferId;
        this.amount = amount;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.dateTimeRequested = dateTimeRequested;
        this.status = status;
    }

    public Transfer(double amount, int senderId, int recipientId, String senderUsername, String recipientUsername) {
        this.amount = amount;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.senderUsername = senderUsername;
        this.recipientUsername = recipientUsername;
    }

    public Transfer(double amount, String senderUsername, String recipientUsername) {
        this.amount = amount;
        this.senderUsername = senderUsername;
        this.recipientUsername = recipientUsername;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }


    public LocalDateTime getDateTimeRequested() {
        return dateTimeRequested;
    }

    @JsonIgnore
    public void setDateTimeRequested(LocalDateTime dateTimeRequested) {
        this.dateTimeRequested = dateTimeRequested;
    }

    public LocalDateTime getDateTimeExecuted() {
        return dateTimeExecuted;
    }

    @JsonIgnore
    public void setDateTimeExecuted(LocalDateTime dateTimeExecuted) {
        this.dateTimeExecuted = dateTimeExecuted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    @Override
    public String toString() {
        return String.format("Transaction #: %s\n Status: %s\n Amount: $%s\n Sender: %s\n Recipient: %s",
                transferId, status, amount, senderUsername, recipientUsername);

    }


}
