/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.validator;

import com.mycompany.atm.domain.Account;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Achmad_ST761
 */
@Component
public class AccountFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return Account.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "accountNumber", "accountNumber.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pin", "pin.required");
        
        Account account = (Account) o;
        if (!genericValidation(account.getAccountNumber()) 
                || !genericValidation(account.getPin())) {
            errors.rejectValue("accountNumber", "account.notValidDigits", null , "");
        }
        
    }
    
    public boolean genericValidation(String strNum) {
        return strNum.length()==6 
                && isNumeric6Digits(strNum);
    }
    
    public boolean isNumeric6Digits(String strNum) {
        return Pattern.matches("[0-9]{6}", strNum);
    }
    
    public boolean isNumeric(String strNum) {
        return Pattern.matches("[0-9]*", strNum);
    }
    
}
