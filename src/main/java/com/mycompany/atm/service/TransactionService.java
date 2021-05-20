/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.service;

import com.mycompany.atm.custom.exception.AccountNumberDuplicatedException;
import com.mycompany.atm.custom.exception.InsufficientBalanceException;
import com.mycompany.atm.custom.exception.DuplicatedRecordException;
import com.mycompany.atm.custom.exception.IncorrectCSVDataException;
import com.mycompany.atm.custom.exception.InvalidAccountException;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.domain.Transaction;
import com.mycompany.atm.domain.TransactionFundTransfer;
import com.mycompany.atm.domain.TransactionWithdraw;
import com.mycompany.atm.repository.AccountRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Achmad_ST761
 */
@AllArgsConstructor
@Service
public class TransactionService {
    
    @Autowired
    private final AccountRepository accountRepository;
    
    public List<Account> readAllFromCSV(String filePath) {
        
        List<Account> listAccounts = new ArrayList<>();
        
        try{    
            listAccounts = accountRepository.readAllFromCSV(filePath);
        } catch (AccountNumberDuplicatedException|DuplicatedRecordException|IOException|IncorrectCSVDataException e) {
            System.out.println("IMPORT INFO : "+e.getMessage());
        }

        return listAccounts;
    }
            
    public Account getAccount(String accountNum, String pin) {
        Account account = accountRepository.getAccount(accountNum, pin);
        if (account.getAccountNumber() == null){
            throw new InvalidAccountException();
        }
        return account;
    }
    
    public void checkAccountAvailability(String accountNum) {
        if (accountRepository.findAccount(accountNum) == null) {
            throw new InvalidAccountException();
        }
    }
    
    public void withdraw(Account userAccount, int amount) {
        if (isUserHaveBalance(new BigDecimal(amount), userAccount.getBalance())) {
            userAccount.updateUserAmount(amount);
            Transaction transaction = new TransactionWithdraw(LocalDateTime.now(), amount*-1);
            userAccount.addUserTransactionHistory(transaction);
            accountRepository.updateAccount(userAccount);
        } else {
            throw new InsufficientBalanceException(userAccount);
        }
    }
    
    public String getRandomRefNum() {
        String temp = "";
        Random ran = new Random();
        for(int i=0;i<=5;i++){
            temp = temp.concat(Integer.toString(ran.nextInt(10)));
        }
        return temp;
    }

    public void fundTransfer(Account userAccount, TransactionFundTransfer transaction) {
        if (isUserHaveBalance(new BigDecimal(transaction.getAmount()), userAccount.getBalance())) {
            Account otherAccount = accountRepository.findAccount(transaction.getDestAccount());
            TransactionFundTransfer otherAccountTransaction = new TransactionFundTransfer();
            Integer amount = transaction.getAmount();
            
            transaction.setTransactionDate(LocalDateTime.now());
            updateAccount(userAccount, transaction, amount*-1);
            
            otherAccountTransaction.setRefNumber(transaction.getRefNumber());
            otherAccountTransaction.setTransactionDate(transaction.getTransactionDate());
            updateAccount(otherAccount, otherAccountTransaction, amount);
            
        } else {
            throw new InsufficientBalanceException(userAccount);
        }
    }

    private boolean isUserHaveBalance(BigDecimal amount, BigDecimal userBalance) {
        return amount.compareTo(userBalance) != 1;
    }
    
    private void updateAccount(Account account, TransactionFundTransfer transaction, Integer amount) {
        account.setBalance(account.getBalance().add(new BigDecimal(amount)));

        transaction.setAmount(amount);
        account.addUserTransactionHistory(transaction);
        accountRepository.updateAccount(account);
    }
    
}
