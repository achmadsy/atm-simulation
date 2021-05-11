/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Achmad_ST761
 */
public class Transaction {
    private LocalDateTime transactionDate;
    private String transactionType;
    private Integer amount;

    public Transaction() {
    }

    public Transaction(LocalDateTime transactionDate, String transactionType, Integer amount) {
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public Transaction(String transactionType) {
        this.transactionType = transactionType;
    }
   
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    
    public String getFormattedDate() {
        return this.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));
    }
    

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
 
}
