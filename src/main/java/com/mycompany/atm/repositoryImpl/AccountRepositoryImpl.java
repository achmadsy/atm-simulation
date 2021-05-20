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
    public Transaction getTransaction(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Transaction> findAllTransaction(Account account) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Account> readAllFromCSV(String filePath) throws IOException, IncorrectCSVDataException, AccountNumberDuplicatedException, DuplicatedRecordException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
}
