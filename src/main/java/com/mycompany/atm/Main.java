/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm;

import com.mycompany.atm.custom.exception.AccountNumberDuplicatedException;
import com.mycompany.atm.custom.exception.DuplicatedRecordException;
import com.mycompany.atm.custom.exception.IncorrectCSVDataException;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.service.TransactionService;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Achmad_ST761
 */
public class Main {
    public static String filePath = "";
    public static List<Account> listAccounts; 
    
    public static void main(String[] args) {
        filePath = args[0];
        TransactionService transactionService = new TransactionService();
        listAccounts = transactionService.getAccounts();
    }
    
}
