/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm.daoImpl;

import com.mycompany.atm.dao.TransactionDao;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.domain.Transaction;
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
       

}
