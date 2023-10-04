package com.techelevator;

import com.techelevator.model.Account;
import com.techelevator.model.Transfer;
import com.techelevator.services.AccountService;
import com.techelevator.services.AuthenticationService;
import com.techelevator.services.ConsoleService;
import com.techelevator.services.TransferService;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class App {

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService();
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        boolean loggedIn = false;
        String username = null;

        while(true){
            while (!loggedIn){
                System.out.println("Welcome!");
                System.out.println("1. Register as new user?");
                System.out.println("2. Log in as existing user?");
                while(true) {
                    int option = consoleService.promptForMenuSelection();
                    if (option == 1) {
                        handleRegister();
                        break;
                    }
                    if (option == 2) {
                        username = handleLogin();
                        break;
                    }
                }
                    if (username != null) {
                        loggedIn = true;
                    }
            }
            if (!displayAccountDetails()){
                break;
            }
            consoleService.printMainMenu();
            int userChoice = consoleService.promptForMenuSelection();
            if (userChoice == 1){
                accountService.createNewAccountForLoggedInUser();
            }
            if (userChoice == 2){
                handleSendTransaction();
            }
            if (userChoice == 3){
                handleRequestTransaction();
            }
            if (userChoice == 4){
                listPendingTransfers();

            }
            if (userChoice == 5){
                approveOrDenyPendingTransactions(username);;
            }
            if (userChoice == 6){
                listAllTransfers(username);
            }
            if (userChoice == 7){
                System.out.println("Enter the unique ID belonging to your transaction:");
                System.out.println("(Transaction Id's will always start with 3)");
                int id = consoleService.promptForInt();
                Transfer transfer = transferService.getTransferById(id);
                if (transfer == null){
                    System.out.println("No such transfer :(");
                } else System.out.println(transfer);

            } if (userChoice == 8){
                break;
            }


        }

    }


    private boolean displayAccountDetails(){
        Account[] accounts = accountService.getAccountsForLoggedInUser();
        if (accounts.length < 1) {
            System.out.println("You do not currently have an account with us, would you like to open one?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            while (true) {
                int userChoice = consoleService.promptForMenuSelection();
                if (userChoice == 1){
                    accountService.createNewAccountForLoggedInUser();
                    break;
                }
                if (userChoice == 2){
                    return false;
                }
            }

        }
        if (accounts.length < 2){
            System.out.println("Your current account balance is: " + accounts[0].getBalance());
            return true;
        }
        System.out.println("You have " + accounts.length + " accounts with us:");
        System.out.println("Account # : Balance");
        for (Account account : accounts)
            System.out.println(account.getAccountId() + " : " + account.getBalance());
        return true;
    }

    private void handleSendTransaction(){
        System.out.println("Who would you like to send money to?");
        String[] users = transferService.getAvailableUsers();
        int option = 1;
        for (String user : users){
            System.out.println("[" + option + "] " + user);
            option++;
        }
        int userIndex = consoleService.promptForMenuSelection() - 1;
        System.out.println("How much would you like to send?");


        double amount = consoleService.promptForDouble();

        Transfer transfer = transferService.sendTransfer(users[userIndex], amount);


        System.out.println("Success!  Here is your receipt:");
        System.out.println(transfer);

    }

    private void handleRequestTransaction(){
        System.out.println("Who would you like to request money from?");
        String[] users = transferService.getAvailableUsers();
        int option = 1;
        for (String user : users){
            System.out.println("[" + option + "] " + user);
            option++;
        }
        int userIndex = consoleService.promptForMenuSelection() - 1;
        System.out.println("How much would you like to request?");


        double amount = consoleService.promptForDouble();

        Transfer transfer = transferService.requestTransfer(users[userIndex], amount);

        System.out.println("Transaction requested!  Here is your receipt:");
        System.out.println(transfer);

    }

    private void listPendingTransfers(){
        Transfer[] transfers = transferService.listPendingTransfersForLoggedInUser();
        if (transfers.length == 0){
            System.out.println("No Pending Transfers");
            return;
        }
        for (Transfer transfer : transfers){
            System.out.println(transfer);
        }
    }

    private void approveOrDenyPendingTransactions(String username) {
        System.out.println("Select a transaction to approve");
        Transfer[] transfers = transferService.listPendingTransfersForLoggedInUser();

        //filter by transfers where are the sender...
        List<Transfer>availableTransfers = new ArrayList<>();
        for (Transfer transfer : transfers){
            if (transfer.getSenderUsername().equals(username)){
                availableTransfers.add(transfer);
            }
        }

        if (availableTransfers.size() == 0){
            System.out.println("No transfers to approve/deny :(");
            return;
        }

        int option = 1;
        for (Transfer transfer : availableTransfers) {
                System.out.println("[" + option + "] " + transfer);
                option++;
        }
        int transferIndex = consoleService.promptForMenuSelection() - 1;
        System.out.println("would you like to approve or deny it?");
        System.out.println("1. Approve");
        System.out.println("2. Deny");
        System.out.println("3. Cancel");
        int userChoice = consoleService.promptForMenuSelection();
        while (true){
            if (userChoice == 1) {
                transferService.approveTransferById(transfers[transferIndex].getTransferId());
                break;
            }
            if (userChoice == 2) {
                transferService.denyTransferById(transfers[transferIndex].getTransferId());
                break;
            }
            if (userChoice == 3){
                break;
            }

        }
    }

    private void listAllTransfers(String username){
        Transfer[] transfers = transferService.listTransfersForLoggedInUser();
        for (Transfer transfer : transfers){
            if(transfer.getSenderUsername().equals(username)){
                System.out.println("Sent:");
                System.out.println(transfer);
            } else if (transfer.getStatus().equals("Pending")) {
                System.out.println("Pending:");
                System.out.println(transfer);
            } else {
                System.out.println("Received:");
                System.out.println(transfer);
            }

        }
    }




    private String handleLogin() {
        String username = consoleService.promptForString("Username: ");
        String password = consoleService.promptForString("Password: ");
        String token = authenticationService.login(username, password);
        if (token != null) {
            accountService.setAuthToken(token);
            transferService.setAuthToken(token);
        } else {
            consoleService.printErrorMessage();
            return null;
        }
        return username;
    }

    private void handleRegister() {
        String username = consoleService.promptForString("Username: ");
        String password = consoleService.promptForString("Password: ");
        authenticationService.register(username, password);
    }

}
