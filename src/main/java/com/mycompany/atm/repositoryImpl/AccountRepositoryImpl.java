/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.repositoryImpl;

import com.mycompany.atm.custom.exception.AccountNumberDuplicatedException;
import com.mycompany.atm.custom.exception.DuplicatedRecordException;
import com.mycompany.atm.custom.exception.IncorrectCSVDataException;
import com.mycompany.atm.dao.AccountDao;
import com.mycompany.atm.dao.TransactionDao;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.domain.Transaction;
import com.mycompany.atm.repository.AccountRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Achmad_ST761
 */
@Component
@AllArgsConstructor
@Transactional
public class AccountRepositoryImpl implements AccountRepository{
    
    @Autowired
    private AccountDao accountDao;
    
    @Autowired
    private TransactionDao transactionDao;

    public AccountRepositoryImpl() {
    }
        
    @Override
    public Account getAccount(String accountNumber, String pin) {
        Account account = accountDao.get(accountNumber, pin);
        if (account.getAccountName() != null) {
            account.setTransactionHistory(transactionDao.findAll(account));
        }
        return account;
    }

    @Override
    public Account findAccount(String accountNumber) {
        Account account = accountDao.find(accountNumber);
        if (account.getAccountName() != null) {
            account.setTransactionHistory(transactionDao.findAll(account));
        }
        return account;
    }

    @Override
    @Transactional
    public void updateAccount(Account account) {
        accountDao.update(account);
    }

    @Override
    public List<Account> findAllAccount() {
        List<Account> accountList = accountDao.findAll();
        accountList.stream().forEach(e -> e.setTransactionHistory(transactionDao.findAll(e)));
        return accountList;
    }

    @Override
    @Transactional
    public void saveAccount(Account account) {
        accountDao.save(account);
    }
    
    @Override
    public void loadAccounts(String csvFilePath) throws IncorrectCSVDataException, AccountNumberDuplicatedException, DuplicatedRecordException, IOException {
        accountDao.loadAccounts(csvFilePath);
    }

    @Override
    public List<Transaction> findLast10TransactionHistory(Account account, LocalDate date) {
        return transactionDao.findLast10TransactionHistory(account, date);
    }
 
}
