/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Size;
import lombok.ToString;

/**
 *
 * @author Achmad_ST761
 */
@Setter
@Getter
@ToString
@Table(name = "accounts", schema = "atm")
@Entity
public class Account implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Size(min = 6, max = 6, message= "Only 6 digits permiteed")
    @Column(name = "account_number", nullable = false)
    private String accountNumber;
    @Column(nullable = false)
    private String pin;
    @Column(name = "account_name", nullable = false)
    private String accountName;
    @Column(nullable = false)
    private BigDecimal balance;
    @OneToMany(targetEntity = Transaction.class, mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transaction> transactionHistory;

    public Account() {
   
    }

    public Account(String accountNumber, String pin, String accountName, BigDecimal balance, ArrayList<Transaction> transactionHistory) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.accountName = accountName;
        this.balance = balance;
        this.transactionHistory = transactionHistory;
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
