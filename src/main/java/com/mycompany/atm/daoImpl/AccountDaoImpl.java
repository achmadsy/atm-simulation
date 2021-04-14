/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.daoImpl;

import com.mycompany.atm.Main;
import com.mycompany.atm.custom.exception.AccountNumberDuplicatedException;
import com.mycompany.atm.custom.exception.DuplicatedRecordException;
import com.mycompany.atm.custom.exception.IncorrectCSVDataException;
import com.mycompany.atm.dao.AccountDao;
import com.mycompany.atm.domain.Account;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Achmad_ST761
 */
public class AccountDaoImpl implements AccountDao {
    
    private final String filePath;

    @Override
    public Account get(String accountNumber, String pin) {
        return Main.listAccounts.stream().filter(e -> e.getAccountNumber().equals(accountNumber)
            && e.getPin().equals(pin)).findFirst().orElse(new Account());
    }

    @Override
    public Account find(String accountNumber) {
        return Main.listAccounts.stream().filter(e -> e.getAccountNumber().equals(accountNumber)).findFirst().orElse(new Account());
    }
    
    @Override
    public void update(String accountNumber, BigDecimal newBalance) {
        Main.listAccounts.stream().filter(e -> e.getAccountNumber().equals(accountNumber)).forEach(x -> {
            x.setBalance(newBalance);
        });
    }

    public AccountDaoImpl() {
        this.filePath = Main.filePath;
    }
       
    @Override
    public List<Account> readAllFromCSV() throws IncorrectCSVDataException, AccountNumberDuplicatedException, DuplicatedRecordException, IOException {
        List<Account> accounts = new ArrayList<>();
        AtomicInteger accountFixedCount = new AtomicInteger(0);
        
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        
        bufferedReader.lines().skip(1).forEach(e -> {
            Account account = null;
            String[] data = e.split(",");
            if (data.length == 4) {
                accounts.add(new Account(data[0],data[1],data[2],new BigDecimal(data[3]), new ArrayList<>()));
            }
            accountFixedCount.getAndIncrement();
        });

        
        String duplicatedAccNumber = checkAccountNumberDuplicate(accounts);
        Account duplicatedRecord = checkDuplicatedRecord(accounts);
        
        if (accounts.size() != accountFixedCount.intValue()) {
            throw new IncorrectCSVDataException();
        } else if (duplicatedAccNumber != null){
            throw new AccountNumberDuplicatedException(duplicatedAccNumber);
        } else if (duplicatedRecord != null) {
            throw new DuplicatedRecordException(duplicatedRecord);
        }

        bufferedReader.close();
        
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
