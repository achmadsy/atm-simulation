/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.service;

import com.mycompany.atm.custom.exception.AccountNumberDuplicatedException;
import com.mycompany.atm.custom.exception.InsufficientBalanceException;
import com.mycompany.atm.custom.exception.DuplicatedRecordException;
import com.mycompany.atm.custom.exception.IncorrectCSVDataException;
import com.mycompany.atm.custom.exception.InvalidAccountException;
import com.mycompany.atm.daoImpl.AccountDaoImpl;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.domain.Transaction;
import com.mycompany.atm.domain.TransactionFundTransfer;
import com.mycompany.atm.domain.TransactionWithdraw;
import com.mycompany.atm.repositoryImpl.AccountRepositoryImpl;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Achmad_ST761
 */
public class TransactionService {
    private final AccountRepositoryImpl accountRepositoryImpl;
    
    public TransactionService() {
        this.accountRepositoryImpl = new AccountRepositoryImpl(new AccountDaoImpl());
    }
    
    public List<Account> readAllFromCSV() {
        
        List<Account> listAccounts = new ArrayList<>();
        
        try{    
            listAccounts = accountRepositoryImpl.readAllFromCSV();
        } catch (AccountNumberDuplicatedException|DuplicatedRecordException|IOException|IncorrectCSVDataException e) {
            System.out.println("IMPORT INFO : "+e);
        }

        return listAccounts;
    }
    
    public List<Account> getDefaultAccounts() {
        List<Account> listAccounts = new ArrayList<>();
        Account default1 = new Account("112233", "012108", "John Doe", new BigDecimal(100), new ArrayList<>());
        Account default2 = new Account("112244", "932012", "Jane Doe", new BigDecimal(30), new ArrayList<>());
        listAccounts.add(default1);
        listAccounts.add(default2);
        
        return listAccounts;
    }
    
    public List<Account> getAccounts() {
        List<Account> listAccontsDefault = this.getDefaultAccounts();
        final List<Account> listAccontsFromCSV = this.readAllFromCSV(); 
           
        List<Account> listAccount = listAccontsDefault.stream().filter(o -> listAccontsFromCSV.stream().anyMatch(csv -> !csv.getAccountName().equals(o.getAccountName())))
                .collect(Collectors.toList());
        
        return Stream.concat(listAccontsFromCSV.stream(), listAccount.stream()).collect(Collectors.toList());
    }
    
    public Account getAccount(String accountNum, String pin) {
        Account account = accountRepositoryImpl.get(accountNum, pin);
        if (account.getAccountNumber() == null){
            throw new InvalidAccountException();
        }
        return account;
    }
    
    public void checkAccountAvailability(String accountNum) {
        if (accountRepositoryImpl.find(accountNum) == null) {
            throw new InvalidAccountException();
        }
    }
    
    public void withdraw(Account userAccount, int amount) {
        if (BigDecimal.valueOf(amount).compareTo(userAccount.getBalance()) != 1) {
            userAccount.updateUserAmount(amount);
            Transaction transaction = new TransactionWithdraw(LocalDateTime.now(), "-"+String.valueOf(amount));
            userAccount.addUserTransactionHistory(transaction);
            accountRepositoryImpl.update(userAccount.getAccountNumber(), userAccount.getBalance());
        } else {
            throw new InsufficientBalanceException(userAccount);
        }
    }
    
    public String getRandomRefNum() {
        String temp = "";
        Random ran = new Random();
        for(int i=0;i<=5;i++){
            temp = temp.concat(Integer.toString(ran.nextInt(10)));
        }
        return temp;
    }

    public void fundTransfer(Account userAccount, TransactionFundTransfer transaction) {
        if (userAccount.getBalance().compareTo(new BigDecimal(transaction.getAmount())) == -1){
            throw new InsufficientBalanceException(userAccount);
        }
        String amount = transaction.getAmount();
        userAccount.setBalance(userAccount.getBalance().subtract(new BigDecimal(transaction.getAmount())));
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAmount("-"+amount);
        userAccount.addUserTransactionHistory(transaction);
        accountRepositoryImpl.update(userAccount.getAccountNumber(), userAccount.getBalance());
        Account otherAccount = accountRepositoryImpl.find(transaction.getDestAccount());
        otherAccount.setBalance(otherAccount.getBalance().add(new BigDecimal(transaction.getAmount())));
        transaction.setAmount("+"+amount);
        otherAccount.addUserTransactionHistory(transaction);
        accountRepositoryImpl.update(otherAccount.getAccountNumber(), otherAccount.getBalance());
    }
    
}