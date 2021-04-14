/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.domain;

import com.mycompany.atm.enums.TransactionType;
import java.time.LocalDateTime;

/**
 *
 * @author Achmad_ST761
 */
public class TransactionWithdraw extends Transaction {

    public TransactionWithdraw(LocalDateTime transactionDate, String amount) {
        super(transactionDate, TransactionType.WITHDRAW.toString() , amount);
    }
   
       
}
