/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.service;

import com.mycompany.atm.custom.exception.AccountNumberException;
import com.mycompany.atm.custom.exception.MultiplyAmountException;
import com.mycompany.atm.custom.exception.InvalidAmountException;
import com.mycompany.atm.custom.exception.InvalidReferenceException;
import com.mycompany.atm.custom.exception.MaximumAmountException;
import com.mycompany.atm.custom.exception.MinimumAmountException;
import com.mycompany.atm.custom.exception.PinException;
import java.math.BigDecimal;
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
    
    public void amountValidation(int amount){
        if (Integer.valueOf(amount)%10!=0) {
            throw new MultiplyAmountException();
        }
        if (new BigDecimal(amount).compareTo(new BigDecimal(1000)) == 1){
            throw new MaximumAmountException();
        }
        if (new BigDecimal(amount).compareTo(new BigDecimal(1)) == -1){
            throw new MinimumAmountException();
        }
    } 
    
    public boolean isNumeric6Digits(String strNum) {
        return Pattern.matches("[0-9]{6}", strNum);
    }
    
    public void checkNumericAmount(String strNum) {
        if (!Pattern.matches("[0-9]*", strNum)){
            throw new InvalidAmountException();
        }
    }

    public void checkRefNumber(String refNumber) {
        if (refNumber.isEmpty()
                || !this.isNumeric6Digits(refNumber)) {
            throw new InvalidReferenceException();
        }
    }
    
}
