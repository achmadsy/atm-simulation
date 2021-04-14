/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.service;

import com.mycompany.atm.custom.exception.AccountNumberException;
import com.mycompany.atm.custom.exception.InsufficientBalanceException;
import com.mycompany.atm.custom.exception.InvalidAccountException;
import com.mycompany.atm.custom.exception.InvalidAmountException;
import com.mycompany.atm.custom.exception.InvalidReferenceException;
import com.mycompany.atm.custom.exception.MaximumAmountException;
import com.mycompany.atm.custom.exception.MinimumAmountException;
import com.mycompany.atm.custom.exception.MultiplyAmountException;
import com.mycompany.atm.custom.exception.PinException;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.domain.Transaction;
import com.mycompany.atm.domain.TransactionFundTransfer;
import java.io.IOException;
import java.math.BigDecimal;
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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
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
        clearScreen();
        
        try {
           Integer userOpt = Integer.valueOf(option); 
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
        clearScreen();
        
        try {
            Integer userOpt = Integer.valueOf(option); 
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
        } catch (InsufficientBalanceException e) {
            System.out.println(e.getMessage());
            showWithdrawScreen();
        }
        
    }

    private void showWithdrawSummaryScreen() {
        userLastTransaction = userAccount.getLatestTransactionHistory();
        System.out.println("Summary");
        System.out.println("Date : "+userLastTransaction.getFormattedDate());
        System.out.println("Withdraw $: "+userLastTransaction.getAmount().substring(1,userLastTransaction.getAmount().length()));
        System.out.println("Balance $: "+userAccount.getBalance().toString());
        System.out.println("");
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.print("Please choose option[2]: ");
        option = scanner.nextLine();
        clearScreen();
        
        try {
           Integer userOpt = Integer.valueOf(option); 
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
        System.out.println("Other Withdraw");
        System.out.print("Enter amount to withdraw: ");
        option = scanner.nextLine();
        clearScreen();
        
        try {
            Integer userAmount = Integer.valueOf(option); 
            validationService.amountValidation(userAmount);
            transactionService.withdraw(userAccount,userAmount);
            showWithdrawSummaryScreen();
        } catch (NumberFormatException e) {
            System.out.println("Invalid Amount");
            showOtherWithdrawScreen();
        } catch (MultiplyAmountException | MaximumAmountException | MinimumAmountException e) {
            System.out.println(e.getMessage());
            showOtherWithdrawScreen();
        }
        
    }

    private void showHistoryScreen() {
        System.out.println("User Transaction History");
        System.out.println("Type                    Amount");
        userAccount.getTransactionHistory().stream().forEach(e -> {
            System.out.println(e.getTransactionType()+"                 "+e.getAmount());
        });
//            System.out.println(userAccount.getTransactionHistory().size());
        System.out.println("Press enter to back to the main menu");
        scanner.nextLine();
        clearScreen();
        showTransactionScreen();
    }
    
    private void showFundTransferAccountScreen() {
        System.out.println("Please enter destination account and");
        System.out.println("press enter to continue or");
        System.out.print("press enter to go back to Transaction: ");
        option = scanner.nextLine();
        clearScreen();
        if (option.isEmpty()) {
            showTransactionScreen();
        } else {
            userLastTransaction = new TransactionFundTransfer();
            ((TransactionFundTransfer) userLastTransaction).setDestAccount(option);
            showFundTransferAmountScreen();
        }
    }

    private void showFundTransferAmountScreen() {
        System.out.println("Please enter transfer amount and press enter to continue or");
        System.out.print("press enter to go back to Transaction: ");
        option = scanner.nextLine();
        clearScreen();
        if (option.isEmpty()) {
            showTransactionScreen();
        } else {
            ((TransactionFundTransfer) userLastTransaction).setAmount(option);
            showFundTransferRefScreen();
        }
    }

    private void showFundTransferRefScreen() {
        String rand = transactionService.getRandomRefNum();
        System.out.println("Reference Number: "+rand+" (This is an autogenerated random 6 digits number)");
        System.out.println("press enter to continue: ");
        ((TransactionFundTransfer) userLastTransaction).setRefNumber(rand);
        scanner.nextLine();
        clearScreen();
        showFundTransferConfirmScreen();
    }

    private void showFundTransferConfirmScreen() {
        System.out.println("Transfer Confirmation");
        System.out.println("Destination Account : "+((TransactionFundTransfer) userLastTransaction).getDestAccount());
        System.out.println("Transfer Amount     : $"+((TransactionFundTransfer) userLastTransaction).getAmount());
        System.out.println("Reference Number    : "+((TransactionFundTransfer) userLastTransaction).getRefNumber());
        System.out.println("");
        System.out.println("1. Confirm Trx");
        System.out.println("2. Cancel Trx");
        System.out.print("Choose option[2]: ");
        option = scanner.nextLine();
        BigDecimal lastTransactionAmount = new BigDecimal(0);
        clearScreen();
        
        try {
            Integer userOpt = Integer.valueOf(option); 
            
            switch (userOpt) {
                case 1:
                    validationService.credentialsValidation("AccountNumber", ((TransactionFundTransfer) userLastTransaction).getDestAccount());
                    transactionService.checkAccountAvailability(((TransactionFundTransfer) userLastTransaction).getDestAccount());
                    validationService.checkNumericAmount(((TransactionFundTransfer) userLastTransaction).getAmount());
                    validationService.amountValidation(Integer.valueOf(((TransactionFundTransfer) userLastTransaction).getAmount()));
                    validationService.checkRefNumber(((TransactionFundTransfer) userLastTransaction).getRefNumber());
                    transactionService.fundTransfer(userAccount, (TransactionFundTransfer) userLastTransaction);
                    showFundTransferSummaryScreen();
                    break;
                case 2:
                    showTransactionScreen();
                    break;
                default:
                    showFundTransferConfirmScreen();
                    break;
            }
                   
        } catch (NumberFormatException e) {
            showFundTransferConfirmScreen();
        } catch (AccountNumberException | InvalidAccountException | InvalidAmountException | MultiplyAmountException | MaximumAmountException | MinimumAmountException |
                InvalidReferenceException | InsufficientBalanceException e) {
            System.out.println(e.getMessage());
            showTransactionScreen();
        }
        
    }

    private void showFundTransferSummaryScreen() {
        userLastTransaction = userAccount.getLatestTransactionHistory();
        System.out.println("Fund Transfer Summary");
        System.out.println("Destination Account : "+((TransactionFundTransfer)userLastTransaction).getDestAccount());
        System.out.println("Transfer Amount     : $"+((TransactionFundTransfer)userLastTransaction).getAmount().substring(1, ((TransactionFundTransfer)userLastTransaction).getAmount().length()));
        System.out.println("Reference Number    : "+((TransactionFundTransfer)userLastTransaction).getRefNumber());
        System.out.println("Balance             : "+userAccount.getBalance());  
        System.out.println("");
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.print("Choose option[2]: ");
        option = scanner.nextLine();
        clearScreen();
        
        try {
            Integer userOpt = Integer.valueOf(option); 
            switch (userOpt){
                case 1:
                    showTransactionScreen();
                    break;
                case 2:
                    showWelcomeScreen();
                    break;
                default: 
                    showFundTransferSummaryScreen();
                    break;
            }
        } catch(NumberFormatException e) {
            showFundTransferSummaryScreen();
        }
    }
    
    
}
