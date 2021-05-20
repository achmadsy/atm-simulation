/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.dao;

import com.mycompany.atm.domain.Account;
import com.mycompany.atm.domain.Transaction;
import java.util.List;

/**
 *
 * @author Achmad_ST761
 */

public interface TransactionDao {
    public Transaction get(long id);
    public void save(Transaction transaction);
    public List<Transaction> findAll(Account account);
    public void update(Transaction transaction);
}
