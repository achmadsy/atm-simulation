/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm;

import com.mycompany.atm.service.MenuService;
import com.mycompany.atm.service.TransactionService;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author Achmad_ST761
 */
@SpringBootApplication
@EntityScan
@ComponentScan("com.mycompany.atm")
public class Main implements ApplicationRunner {
    
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    
    @Value("${filePath}")
    private static String filePath = "";
    
    public static void main(String[] args) {
        if (args.length > 0) {
            filePath = args[0];
        }
        TransactionService transactionService = new TransactionService(filePath);
        MenuService menuService = new MenuService(transactionService);
        
        try {
            SpringApplication.run(Main.class, args);
        } catch (NoSuchElementException e) {
            System.exit(0);
        }
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("CSV filepath: "+filePath);
    }
    
}
