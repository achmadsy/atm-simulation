/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.dao;

import com.mycompany.atm.custom.exception.AccountNumberDuplicatedException;
import com.mycompany.atm.custom.exception.DuplicatedRecordException;
import com.mycompany.atm.custom.exception.IncorrectCSVDataException;
import com.mycompany.atm.domain.Account;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Achmad_ST761
 */
public interface AccountDao {
    public Account get(String accountNumber, String pin);
    public Account find(String accountNumber);
    public void update(String accountNumber, BigDecimal newBalance);
    public List<Account> readAllFromCSV() throws IncorrectCSVDataException, AccountNumberDuplicatedException, DuplicatedRecordException, IOException;
}
