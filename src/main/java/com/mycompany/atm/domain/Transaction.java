/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.domain;

import java.time.LocalDate;

/**
 *
 * @author Achmad_ST761
 */
public class Transaction {
    private LocalDate transactionDate;
    private String transactionType;
    private String amount;

    public Transaction() {
    }

    public Transaction(LocalDate transactionDate, String transactionType, String amount) {
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public Transaction(String transactionType) {
        this.transactionType = transactionType;
    }
   
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
 
    
}
