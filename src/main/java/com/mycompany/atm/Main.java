/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm;

import com.mycompany.atm.domain.Account;
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
    }
    
}
