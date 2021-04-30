/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.daoImpl;

import com.mycompany.atm.custom.exception.AccountNumberDuplicatedException;
import com.mycompany.atm.custom.exception.DuplicatedRecordException;
import com.mycompany.atm.custom.exception.IncorrectCSVDataException;
import com.mycompany.atm.dao.AccountDao;
import com.mycompany.atm.domain.Account;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Achmad_ST761
 */
public class AccountDaoImpl implements AccountDao {
    
    private List<Account> listAccounts; 

    @Override
    public Account get(String accountNumber, String pin) {
        return this.listAccounts.stream().filter(e -> e.getAccountNumber().equals(accountNumber)
            && e.getPin().equals(pin)).findFirst().orElse(new Account());
    }

    @Override
    public Account find(String accountNumber) {
        return this.listAccounts.stream().filter(e -> e.getAccountNumber().equals(accountNumber)).findFirst().orElse(new Account());
    }
    
    @Override
    public void update(String accountNumber, BigDecimal newBalance) {
        this.listAccounts.stream().filter(e -> e.getAccountNumber().equals(accountNumber)).forEach(x -> {
            x.setBalance(newBalance);
        });
    }

    public AccountDaoImpl(List<Account> accounts) {
        this.listAccounts = accounts;
    }
       
    @Override
    public List<Account> readAllFromCSV(String filePath) throws IncorrectCSVDataException, AccountNumberDuplicatedException, DuplicatedRecordException, IOException {
        List<Account> accounts = new ArrayList<>();
        Integer accountCounts = Files.readAllLines(Paths.get(filePath)).size()-1;
        
        if (!filePath.isEmpty()) {
                    
            accounts = Files.readAllLines(Paths.get(filePath)).stream().skip(1).map(line -> {
                Account account = null;
                String[] data = line.split(",");
                if (data.length == 4) {
                    return new Account(data[0],data[1],data[2],new BigDecimal(data[3]), new ArrayList<>());
                }
                return null;
            }).filter(account -> account != null).collect(Collectors.toList());

            String duplicatedAccNumber = checkAccountNumberDuplicate(accounts);
            Account duplicatedRecord = checkDuplicatedRecord(accounts);

            if (accounts.size() != accountCounts) {
                throw new IncorrectCSVDataException();
            } else if (duplicatedRecord != null) {
                throw new DuplicatedRecordException(duplicatedRecord);
            } else if (duplicatedAccNumber != null){
                throw new AccountNumberDuplicatedException(duplicatedAccNumber);
            } 
        
        }
        
        return accounts;
    }

    private String checkAccountNumberDuplicate(List<Account> accounts) {
        String accNumber = "";
        for (Account acc: accounts) {
            if (accNumber.equals(acc.getAccountNumber())) {
                return accNumber;
            }
            accNumber = acc.getAccountNumber();
        }
        return null;
    }

    private Account checkDuplicatedRecord(List<Account> accounts) {
        Account accTemp = new Account();
        for (Account acc: accounts) {
            if (acc.equals(accTemp)) {
                return accTemp;
            }
            accTemp = acc;
        }
        return null;
    }

}
