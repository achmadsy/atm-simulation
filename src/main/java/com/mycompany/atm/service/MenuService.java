/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.service;

import com.mycompany.atm.custom.exception.AccountNumberException;
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
            showTransactionScreen();
        } catch (AccountNumberException | PinException | InvalidAccountException e) {
            clearScreen();
            System.out.println(e);
            showWelcomeScreen();
        }  
    }

    public void showTransactionScreen() {
        
    }
    
    
}
