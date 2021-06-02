/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.service;

import com.mycompany.atm.custom.exception.AccountNumberDuplicatedException;
import com.mycompany.atm.custom.exception.AccountNumberException;
import com.mycompany.atm.custom.exception.InsufficientBalanceException;
import com.mycompany.atm.custom.exception.DuplicatedRecordException;
import com.mycompany.atm.custom.exception.IncorrectCSVDataException;
import com.mycompany.atm.custom.exception.InvalidAccountException;
import com.mycompany.atm.custom.exception.InvalidReferenceException;
import com.mycompany.atm.custom.exception.MaximumAmountException;
import com.mycompany.atm.custom.exception.MinimumAmountException;
import com.mycompany.atm.custom.exception.MultiplyAmountException;
import com.mycompany.atm.custom.exception.PinException;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.domain.Transaction;
import com.mycompany.atm.domain.TransactionFundTransfer;
import com.mycompany.atm.domain.TransactionWithdraw;
import com.mycompany.atm.repository.AccountRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpSession;
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
    
    @Autowired
    private final ValidationService validationService;
             
    public Account getAccount(String accountNum, String pin) {
        Account account = accountRepository.getAccount(accountNum, pin);
        if (account.getAccountNumber() == null){
            throw new InvalidAccountException();
        }
        return account;
    }
    
    public void checkAccountAvailability(String accountNum) {
        if (accountRepository.findAccount(accountNum).getAccountNumber() == null) {
            throw new InvalidAccountException();
        }
    }
    
    public Account withdraw(Account userAccount, int amount) {
        userAccount = this.refreshAccount(userAccount);
        validationService.amountValidation(amount);
        if (isUserHaveBalance(new BigDecimal(amount), userAccount.getBalance())) {
            userAccount.updateUserAmount(amount);
            Transaction transaction = new TransactionWithdraw(LocalDateTime.now(), amount*-1);
            transaction.setAccount(userAccount);
            userAccount.addUserTransactionHistory(transaction);
            accountRepository.updateAccount(userAccount);
        } else {
            throw new InsufficientBalanceException(userAccount);
        }
        
        return userAccount;
    }
    
    public String getRandomRefNum() {
        String temp = "";
        Random ran = new Random();
        for(int i=0;i<=5;i++){
            temp = temp.concat(Integer.toString(ran.nextInt(10)));
        }
        return temp;
    }

    public Account fundTransfer(Account userAccount, TransactionFundTransfer transaction) throws InsufficientBalanceException, AccountNumberException, PinException, InvalidAccountException, InvalidReferenceException, MultiplyAmountException, MaximumAmountException, MinimumAmountException {
        
        userAccount = this.refreshAccount(userAccount);
        validationService.credentialsValidation("AccountNumber", transaction.getDestAccount());
        this.checkAccountAvailability(transaction.getDestAccount());
        validationService.amountValidation(transaction.getAmount());
        validationService.checkRefNumber(transaction.getRefNumber());
        
        if (isUserHaveBalance(new BigDecimal(transaction.getAmount()), userAccount.getBalance())) {
            Integer amount = transaction.getAmount();
            updateAccount(userAccount, transaction, amount*-1);
            
            Account otherAccount = accountRepository.findAccount(transaction.getDestAccount());
            TransactionFundTransfer otherAccountTransaction = new TransactionFundTransfer();
            otherAccountTransaction.setRefNumber(transaction.getRefNumber());
            otherAccountTransaction.setTransactionDate(transaction.getTransactionDate());
            updateAccount(otherAccount, otherAccountTransaction, amount);
            
        } else {
            throw new InsufficientBalanceException(userAccount);
        }
        
        return userAccount;
    }

    private boolean isUserHaveBalance(BigDecimal amount, BigDecimal userBalance) {
        return amount.compareTo(userBalance) != 1;
    }
    
    private void updateAccount(Account account, TransactionFundTransfer transaction, Integer amount) {
        account.setBalance(account.getBalance().add(new BigDecimal(amount)));
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAmount(amount);
        transaction.setAccount(account);
        account.addUserTransactionHistory(transaction);
        accountRepository.updateAccount(account);
    }
    
    public Account refreshAccount(Account account){
        return accountRepository.findAccount(account.getAccountNumber());
    }

    public void loadAccounts(String csvFilePath) throws IncorrectCSVDataException, AccountNumberDuplicatedException, DuplicatedRecordException, IOException {
        accountRepository.loadAccounts(csvFilePath);
    }
    
    public List<Transaction> getLast10Transaction(Account account, LocalDate compareDate) {
        return accountRepository.findLast10TransactionHistory(account, compareDate);
    }
    
    public TransactionFundTransfer setNewFundTransaction(HttpSession httpSession){
        TransactionFundTransfer newTransaction = new TransactionFundTransfer();
        newTransaction.setDestAccount((String) httpSession.getAttribute("fund_transfer_destAccNumber"));
        newTransaction.setAmount(Integer.valueOf((String) httpSession.getAttribute("fund_transfer_amount")));
        newTransaction.setRefNumber((String)httpSession.getAttribute("fund_transfer_refNumber"));
        return newTransaction;
    }
    
}
