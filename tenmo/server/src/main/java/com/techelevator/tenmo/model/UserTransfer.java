package com.techelevator.tenmo.model;

import java.time.LocalDateTime;

public class UserTransfer {

    private double amount;
    private String senderUsername;
    private String recipientUsername;
    private LocalDateTime datetimeRequested;
    private LocalDateTime datetimeExecuted;

    public UserTransfer(Transfer transfer){
        this.amount = transfer.getAmount();
        this.datetimeRequested = transfer.getDateTimeRequested();
        this.datetimeExecuted = transfer.getDateTimeExecuted();
        this.senderUsername = transfer.getSenderUsername();
        this.recipientUsername = transfer.getRecipientUsername();
    }


    //method that will let us check if the account asking for this was the sender or recipient:
    public boolean isSender(String sender){
        return sender.equals(this.senderUsername);
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSender() {
        return senderUsername;
    }

    public void setSender(String sender) {
        this.senderUsername = sender;
    }

    public String getRecipient() {
        return recipientUsername;
    }

    public void setRecipient(String recipient) {
        this.recipientUsername = recipient;
    }

    public LocalDateTime getDatetimeRequested() {
        return datetimeRequested;
    }

    public void setDatetimeRequested(LocalDateTime datetimeRequested) {
        this.datetimeRequested = datetimeRequested;
    }

    public LocalDateTime getDatetimeExecuted() {
        return datetimeExecuted;
    }

    public void setDatetimeExecuted(LocalDateTime datetimeExecuted) {
        this.datetimeExecuted = datetimeExecuted;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "amount=" + amount +
                ", sender='" + senderUsername + '\'' +
                ", recipient='" + recipientUsername + '\'' +
                ", datetimeRequested=" + datetimeRequested +
                ", datetimeExecuted=" + datetimeExecuted +
                '}';
    }
}
