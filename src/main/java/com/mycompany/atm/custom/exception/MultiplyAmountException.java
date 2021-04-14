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
public class MultiplyAmountException extends RuntimeException{

    public MultiplyAmountException() {
        super("Invalid Amount, Amount must be multiply of 10");
    }
    
}
