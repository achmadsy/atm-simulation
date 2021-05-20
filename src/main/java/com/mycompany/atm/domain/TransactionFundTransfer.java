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
@DiscriminatorValue("FUND_TRANSFER")
public class TransactionFundTransfer extends Transaction {
    
    private String destAccount;
    private String refNumber;
    
    public TransactionFundTransfer(LocalDateTime transactionDate, Integer amount, String destAccount, String refNumber) {
        super(transactionDate, TransactionType.FUND_TRANSFER.toString(), amount);
        this.destAccount = destAccount;
        this.refNumber = refNumber;
    }

    public TransactionFundTransfer() {
        super(TransactionType.FUND_TRANSFER.toString());
    }   
    
}
