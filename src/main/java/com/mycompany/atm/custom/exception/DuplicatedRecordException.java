/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.custom.exception;

import com.mycompany.atm.domain.Account;

/**
 *
 * @author Achmad_ST761
 */
public class DuplicatedRecordException extends RuntimeException{

    public DuplicatedRecordException(Account account) {
        super("There can't be duplicated records "+account.toString());
    }
    
}
