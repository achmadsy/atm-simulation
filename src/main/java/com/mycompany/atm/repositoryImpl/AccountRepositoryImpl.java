/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.repositoryImpl;

import com.mycompany.atm.custom.exception.AccountNumberDuplicatedException;
import com.mycompany.atm.custom.exception.DuplicatedRecordException;
import com.mycompany.atm.custom.exception.IncorrectCSVDataException;
import com.mycompany.atm.daoImpl.AccountDaoImpl;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.repository.AccountRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Achmad_ST761
 */
public class AccountRepositoryImpl implements AccountRepository{
    
    private final AccountDaoImpl accountDaoImpl;
    
    public AccountRepositoryImpl(AccountDaoImpl accountDaoImpl) {
        this.accountDaoImpl = accountDaoImpl;
    }
    
    @Override
    public Account get(String accountNumber, String pin) {
        return accountDaoImpl.get(accountNumber, pin);
    }

    @Override
    public Account find(String accountNumber) {
        return accountDaoImpl.find(accountNumber);
    }

    @Override
    public List<Account> readAllFromCSV() throws IOException, IncorrectCSVDataException, AccountNumberDuplicatedException, DuplicatedRecordException {
        return accountDaoImpl.readAllFromCSV();
    }

    @Override
    public void update(String accountNumber, BigDecimal newBalance) {
        accountDaoImpl.update(accountNumber, newBalance);
    }
   
    
    
}
