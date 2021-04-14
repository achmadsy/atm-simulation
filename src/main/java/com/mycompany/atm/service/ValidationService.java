/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.service;

import com.mycompany.atm.custom.exception.AccountNumberException;
import com.mycompany.atm.custom.exception.PinException;
import java.util.regex.Pattern;

/**
 *
 * @author Achmad_ST761
 */
public class ValidationService {

    public ValidationService() {
    }
    
    public void credentialsValidation(String type, String strNum) {
        if (strNum.length()!=6 
                || !isNumeric6Digits(strNum)){
                
            switch (type) {
                case "AccountNumber": 
                    throw new AccountNumberException();
                case "Pin":
                    throw new PinException();
                    
            }
            
        }
          
    }
    
    public boolean isNumeric6Digits(String strNum) {
        return Pattern.matches("[0-9]{6}", strNum);
    }
    
    public boolean isNumeric(String strNum) {
        return Pattern.matches("[0-9]*", strNum);
    }
    
}
