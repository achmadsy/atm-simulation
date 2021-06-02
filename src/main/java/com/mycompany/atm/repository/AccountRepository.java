/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.repository;

import com.mycompany.atm.custom.exception.AccountNumberDuplicatedException;
import com.mycompany.atm.custom.exception.DuplicatedRecordException;
import com.mycompany.atm.custom.exception.IncorrectCSVDataException;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.domain.Transaction;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Achmad_ST761
 */
public interface AccountRepository {
    public Account getAccount(String accountNumber, String pin) ;
    public Account findAccount(String accountNumber) ;
    public List<Account> findAllAccount();
    public void updateAccount(Account account);
    public void saveAccount(Account account);
    public void loadAccounts(String csvFilePath) throws IncorrectCSVDataException, AccountNumberDuplicatedException, DuplicatedRecordException, IOException;
    public List<Transaction> findLast10TransactionHistory(Account account, LocalDate date);
}
