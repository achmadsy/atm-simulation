/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm;

import com.mycompany.atm.custom.exception.AccountNumberDuplicatedException;
import com.mycompany.atm.custom.exception.DuplicatedRecordException;
import com.mycompany.atm.custom.exception.IncorrectCSVDataException;
import com.mycompany.atm.dao.AccountDao;
import com.mycompany.atm.daoImpl.AccountDaoImpl;
import com.mycompany.atm.repositoryImpl.AccountRepositoryImpl;
import com.mycompany.atm.service.MenuCLIService;
import com.mycompany.atm.service.TransactionService;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Achmad_ST761
 */
@SpringBootApplication
@EntityScan
@ComponentScan("com.mycompany.atm")
@Configuration
public class Main implements ApplicationRunner {
    
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    
    @Value("${filePath}")
    private static String filePath = "";
        
    public static void main(String[] args) {
        try {
            ApplicationContext context = SpringApplication.run(Main.class, args);    
            
            AccountDaoImpl accountDaoImpl = context.getBean(AccountDaoImpl.class);
            accountDaoImpl.loadAccounts(filePath);
            
            MenuCLIService menuService = new MenuCLIService(context.getBean(TransactionService.class));
            menuService.clearScreen();
            menuService.showWelcomeScreen();
            
        } catch (NoSuchElementException e) {
            System.exit(0);
        } catch (AccountNumberDuplicatedException | DuplicatedRecordException | IOException | IncorrectCSVDataException e) {
            System.out.println(e);
        } 
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("CSV filepath: "+filePath);
    }
}
