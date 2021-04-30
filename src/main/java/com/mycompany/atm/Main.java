/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm;

import com.mycompany.atm.service.MenuService;
import com.mycompany.atm.service.TransactionService;
import java.util.NoSuchElementException;

/**
 *
 * @author Achmad_ST761
 */
public class Main {
    private static String filePath = "";
    
    public static void main(String[] args) {
        if (args.length > 0) {
            filePath = args[0];
        }
        TransactionService transactionService = new TransactionService(filePath);
        MenuService menuService = new MenuService(transactionService);
        menuService.clearScreen();
        try {
            menuService.showWelcomeScreen();
        } catch (NoSuchElementException e) {
            System.exit(0);
        }
    }
    
}
