/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.service;

import com.mycompany.atm.custom.exception.AccountNumberException;
import com.mycompany.atm.custom.exception.BalanceException;
import com.mycompany.atm.custom.exception.InvalidAccountException;
import com.mycompany.atm.custom.exception.PinException;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.domain.Transaction;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Achmad_ST761
 */
public class MenuService {
    
    private final Scanner scanner = new Scanner(System.in);
    private final ValidationService validationService = new ValidationService();
    private final TransactionService transactionService = new TransactionService();
    private Account userAccount;
    private Transaction userLastTransaction;
    private String option;
    
    public MenuService() {
    }

    public void clearScreen() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (final IOException | InterruptedException e) {
            System.out.println(e);
        }
    }

    public void showWelcomeScreen() {
        String accountNum, pin;
        userAccount = null;
        System.out.print("Enter Account Number: ");
        accountNum = scanner.nextLine();
        
        try {
            validationService.credentialsValidation("AccountNumber", accountNum);
            System.out.print("Enter PIN: ");
            pin = scanner.nextLine();
            validationService.credentialsValidation("Pin", pin);
            userAccount = transactionService.getAccount(accountNum, pin);
            clearScreen();
            showTransactionScreen();
        } catch (AccountNumberException | PinException | InvalidAccountException e) {
            clearScreen();
            System.out.println(e);
            showWelcomeScreen();
        }  
    }

    private void showTransactionScreen() {
        System.out.println("1. Withdraw");
        System.out.println("2. Fund Transfer");
        System.out.println("3. Transaction History");
        System.out.println("4. Exit");
        System.out.print("Please choose option[3]: ");
        option = scanner.nextLine();
        
        try {
           Integer userOpt = Integer.valueOf(option); 
           clearScreen();
           switch (userOpt) {
               case 1:
                    showWithdrawScreen();
                    break;
               case 2:
                    showFundTransferAccountScreen();
                    break;
               case 3:
                    showHistoryScreen();
                    break;
               case 4:
                    showWelcomeScreen();
                    break;
               default:
                    showTransactionScreen();
                    break;
           }
        } catch (NumberFormatException e) {
            showTransactionScreen();
        }
        
    }

    private void showWithdrawScreen() {
        System.out.println("1. $10");
        System.out.println("2. $50");
        System.out.println("3. $100");
        System.out.println("4. Other");
        System.out.println("5. Back");
        System.out.print("Please choose option[5]: ");
        option = scanner.nextLine();
        
        try {
            Integer userOpt = Integer.valueOf(option); 
            clearScreen();
            switch (userOpt) {
               case 1:
                    transactionService.withdraw(userAccount,10);
                    showWithdrawSummaryScreen();
                    break;
               case 2:
                    transactionService.withdraw(userAccount,50);
                    showWithdrawSummaryScreen();
                    break;
               case 3:
                    transactionService.withdraw(userAccount,100);
                    showWithdrawSummaryScreen();
                    break;
               case 4:
                    showOtherWithdrawScreen();
                    break;
               case 5:
                    showTransactionScreen();;
                    break;
               default:
                    showWithdrawScreen();
                    break;
           }
        } catch (NumberFormatException e) {
            showWithdrawScreen();
        } catch (BalanceException e) {
            System.out.println(e);
            showWithdrawScreen();
        }
        
    }

    private void showWithdrawSummaryScreen() {
        userLastTransaction = userAccount.getLatestTransactionHistory();
        System.out.println("Summary");
        System.out.println("Date : "+userLastTransaction.getFormattedDate());
        System.out.println("Withdraw : "+userLastTransaction.getAmount());
        System.out.println("Balance : "+userAccount.getBalance().toString());
        System.out.println("");
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.print("Please choose option[2]: ");
        option = scanner.nextLine();
        
        try {
           Integer userOpt = Integer.valueOf(option); 
           clearScreen();
           switch (userOpt) {
               case 1:
                    showTransactionScreen();
                    break;
               case 2:
                    showWelcomeScreen();
                    break;
               default:
                    showWithdrawSummaryScreen();
                    break;
           }
        } catch (NumberFormatException e) {
            showWithdrawSummaryScreen();
        }
        
    }

    private void showOtherWithdrawScreen() {
        
    }

    private void showFundTransferAccountScreen() {
        
    }

    private void showHistoryScreen() {
        
    }
    
    
    
    
}
