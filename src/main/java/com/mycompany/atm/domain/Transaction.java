/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Achmad_ST761
 */
@Setter
@Getter
@Table(name= "transactions", schema = "atm")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="transactionType", 
  discriminatorType = DiscriminatorType.STRING)
public class Transaction implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime transactionDate;
    @Column(name="transactionType", insertable = false, updatable = false)
    private String transactionType;
    private Integer amount;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, updatable = false, insertable = true)
    private Account account;

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
   
    public String getFormattedDate() {
        return this.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));
    }
    
}
