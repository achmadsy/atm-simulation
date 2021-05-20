/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atm;

import com.mycompany.atm.controller.MenuController;
import com.mycompany.atm.custom.exception.AccountNumberDuplicatedException;
import com.mycompany.atm.custom.exception.DuplicatedRecordException;
import com.mycompany.atm.custom.exception.IncorrectCSVDataException;
import com.mycompany.atm.service.MenuService;
import com.mycompany.atm.service.TransactionService;
import com.mycompany.atm.service.ValidationService;
import com.mycompany.atm.validator.AccountFormValidator;
import java.io.IOException;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
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
            SpringApplication.run(Main.class, args);
            TransactionService transactionService = new TransactionService(filePath);
            MenuService menuService = new MenuService(transactionService);
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
    
    @Bean
    public TransactionService transactionService() throws IncorrectCSVDataException, AccountNumberDuplicatedException, DuplicatedRecordException, IOException {
      return new TransactionService(filePath);
   }
    
}
