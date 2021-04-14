/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.custom.exception;

/**
 *
 * @author Achmad_ST761
 */
public class AccountNumberDuplicatedException extends RuntimeException{

    public AccountNumberDuplicatedException(String accountNumber) {
        super("There can't be 2 different accounts with the same Account Number "+accountNumber);
    }
    
}
