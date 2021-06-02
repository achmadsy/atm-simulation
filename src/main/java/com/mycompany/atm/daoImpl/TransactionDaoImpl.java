/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.daoImpl;

import com.mycompany.atm.dao.TransactionDao;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.domain.Transaction;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Achmad_ST761
 */
@Repository
public class TransactionDaoImpl implements TransactionDao {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Transaction get(long id) {
        return (Transaction) entityManager.createQuery("select a from Transaction a where a.id = :id")
                .setParameter("id", id)
                .getResultStream().findFirst().orElse(null);
    }
    
    @Override
    public List<Transaction> findAll(Account account){
        return entityManager.createQuery("select a from Transaction a where a.account = :account")
                .setParameter("account", account)
                .getResultList();
    }
        
    @Override
    public void save(Transaction transaction) {
        entityManager.persist(transaction);
    }
    
    @Override
    public void update(Transaction transaction) {
        entityManager.merge(transaction);
    }

    public TransactionDaoImpl() {
        
    }

    @Override
    public List<Transaction> findLast10TransactionHistory(Account account, LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return entityManager.createQuery("select a from Transaction a where a.account = :account and to_char(a.transactionDate, 'yyyy-MM-dd') = :date order by a.id desc")
                .setParameter("account", account)
                .setParameter("date", date.format(dateTimeFormatter))
                .setMaxResults(10)
                .getResultList();
    }
       

}
