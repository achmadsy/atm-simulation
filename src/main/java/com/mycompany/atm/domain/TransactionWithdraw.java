/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.domain;

import com.mycompany.atm.enums.TransactionType;
import java.time.LocalDateTime;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Achmad_ST761
 */
@Setter
@Getter
@Entity
@DiscriminatorValue("WITHDRAW")
public class TransactionWithdraw extends Transaction {

    public TransactionWithdraw(LocalDateTime transactionDate, Integer amount) {
        super(transactionDate, TransactionType.WITHDRAW.toString() , amount);
    }
   
    public TransactionWithdraw() {
        super(TransactionType.WITHDRAW.toString());
    }
    
}
