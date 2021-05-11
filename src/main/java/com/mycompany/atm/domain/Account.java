/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.domain;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author Achmad_ST761
 */
public class Account {
    private String accountNumber;
    private String pin;
    private String accountName;
    private BigDecimal balance;
    private ArrayList<Transaction> transactionHistory;

    public Account() {
    }

    public Account(String accountNumber, String pin, String accountName, BigDecimal balance, ArrayList<Transaction> transactionHistory) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.accountName = accountName;
        this.balance = balance;
        this.transactionHistory = transactionHistory;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(ArrayList<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    @Override
    public String toString() {
        return "Account{" + "accountNumber=" + accountNumber + ", pin=" + pin + ", accountName=" + accountName + ", balance=" + balance + ", transactionHistory=" + transactionHistory + '}';
    }
 
    public void updateUserAmount(int amount) {
        this.setBalance(this.getBalance().subtract(new BigDecimal(amount)));
    }

    public void addUserTransactionHistory(Transaction transactionHistory) {
        this.transactionHistory.add(transactionHistory);
    }
    
    public Transaction getLatestTransactionHistory(){
        return this.transactionHistory.stream().filter(e -> e.getAmount() < 0).reduce((first, second) -> second).get();
    }
    
    
}
