package com.mycompany.atm.controller;

import com.mycompany.atm.custom.exception.BindingException;
import com.mycompany.atm.custom.exception.InvalidAccountException;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.service.TransactionService;
import com.mycompany.atm.service.ValidationService;
import com.mycompany.atm.validator.AccountFormValidator;
import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Achmad_ST761
 */
@Controller
@AllArgsConstructor
public class LoginController {
    
    @Autowired
    private final TransactionService transactionService;

    @Autowired
    private final AccountFormValidator validator;
    
    @InitBinder
    private void initBinder(WebDataBinder binder) {
        if (binder.getTarget() != null 
                && validator.supports(binder.getTarget().getClass())){
            binder.setValidator(validator);
        }
    }
   
    @GetMapping(value = "/")
    public String defaultPage(ModelMap model) {
        return "redirect:welcome";
    }
    
    @GetMapping(value = "/welcome")
    public String showWelcomePage(ModelMap model) {
        if (model.get("account") == null) {
            model.put("account", new Account());
        }
        return "welcome";
    }
    
    @PostMapping(value = "/login")
    public ModelAndView doLogin(@ModelAttribute("account") @Validated Account account,
            BindingResult bindingResult, ModelMap model, RedirectAttributes ra, HttpSession httpSession) {
        Account accountRetrieved;
               
        try {
            if (bindingResult.hasErrors()) {
                throw new BindingException();
            }
            accountRetrieved = transactionService.getAccount(account.getAccountNumber(), account.getPin()); 
            httpSession.setAttribute("account", accountRetrieved);
            return new ModelAndView ("redirect:main");
        } catch (InvalidAccountException e){
            ra.addFlashAttribute("accountError", e.getMessage());
            return new ModelAndView("redirect:welcome") ;
        } catch (BindingException ex) {    
            ra.addFlashAttribute("org.springframework.validation.BindingResult.account", bindingResult);
            ra.addFlashAttribute("account", account);
            return new ModelAndView("redirect:welcome");
        }
    }
    
    @GetMapping(value = "/main")
    public String showMainPage(ModelMap model, HttpSession httpSession) {
        if (httpSession.getAttribute("account") != null) {
            model.put("account", httpSession.getAttribute("account"));
        } else {
            return "redirect:welcome";
        }
        
        httpSession.setAttribute("newTransaction", null);
        httpSession.setAttribute("transferProgress", null);
        return "main";
    }
    
    @GetMapping(value = "/logout")
    public String doLogout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:welcome";
    }
         
}
