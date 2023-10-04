package com.techelevator.services;

import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection() {
        int menuSelection;
        System.out.print("Please choose an option: ");
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public int promptForInt() {
        int value;
        try {
            value = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            value = -1;
        }
        return value;
    }

    public double promptForDouble() {
        double amount;
        System.out.print("Please enter an amount: ");
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            amount = -1;
        }
        return amount;
    }





    public void printMainMenu() {
        System.out.println();
        System.out.println("1: Open up a new account");
        System.out.println("2: Send money");
        System.out.println("3: Request money");
        System.out.println("4: View pending transactions");
        System.out.println("5: Approve or Deny pending transactions");
        System.out.println("6: View transaction history");
        System.out.println("7: Look up Transfer by ID");
        System.out.println("8: Exit");
        System.out.println();
    }




    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

}
