package com.mycompany.atm.controller;

import com.mycompany.atm.domain.Account;
import com.mycompany.atm.domain.TransactionFundTransfer;
import com.mycompany.atm.service.TransactionService;
import com.mycompany.atm.service.ValidationService;
import com.mycompany.atm.validator.AccountFormValidator;
import java.math.BigDecimal;
import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
public class MenuController {
    
    @Autowired
    private final TransactionService transactionService;

    @Autowired
    private final ValidationService validationService;

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
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.account", bindingResult);
            ra.addFlashAttribute("account", account);
            return new ModelAndView("redirect:welcome");
        }
       
//        Account accountRetrieved = transactionService.checkRecords(account.getAccountNumber(), account.getPin());
//        if (accountRetrieved == null) {
//            ra.addFlashAttribute("accountError", "Invalid Account Number/PIN");
//            return new ModelAndView("redirect:welcome") ;
//        }
//        httpSession.setAttribute("account", accountRetrieved);
        return new ModelAndView ("redirect:main");
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
    
    @GetMapping(value = "/withdraw")
    public String showWithdrawOptionPage(HttpSession httpSession){
        if (httpSession.getAttribute("account") == null) {
            return "redirect:welcome";
        }
        
        httpSession.setAttribute("withdratOther", false);
        return "withdraw";
    }
    
    @GetMapping(value = "/withdraw/{amount}")
    public String showWithdrawSummaryPage(HttpSession httpSession, 
            @PathVariable(name = "amount") Integer amount, Model model,
            RedirectAttributes ra){
        if (httpSession.getAttribute("account") == null
                || amount == null 
                ) {
            return "redirect:/welcome";
        } 
        if (amount % 10 == 0) {
//            if (new BigDecimal(amount).compareTo(new BigDecimal(1000)) == 1) {
//                ra.addFlashAttribute("error", "Maximum amount to withdraw is $1000");
//                return "redirect:/withdraw/other";
//            } else {
//                Account account = (Account) httpSession.getAttribute("account");
//                if (transactionService.validationWithdraw(account, amount)){
//                    transactionService.deductWithdrawAmount(account, amount);
//                    httpSession.setAttribute("account", account);
//                    model.addAttribute("account", account);
//                    return "/withdraw_summary";
//                } else {
//                    ra.addFlashAttribute("error", "Insufficient balance $"+account.getBalance().toString());
//                    Boolean redirect = (Boolean) httpSession.getAttribute("withdratOther");
//                    return redirect ? "redirect:/withdraw/other" : "redirect:/withdraw";
//                }
//            }
return null;
        } else {
            ra.addFlashAttribute("error", "Invalid Amount");
            return "redirect:/withdraw/other";
        }
    }
    
    @GetMapping(value = "/withdraw/other")
    public String showWithdrawOtherPage(HttpSession httpSession){
        if (httpSession.getAttribute("account") == null
                ) {
            return "redirect:/welcome";
        } 
        httpSession.setAttribute("withdratOther", true);
        return "withdraw_other";
    }
    
    @GetMapping(value = "/transfer")
    public String showTransferPage(HttpSession httpSession, Model model){
        if (httpSession.getAttribute("account") == null
                ) {
            return "redirect:/welcome";
        } 
        Integer progress = (Integer) httpSession.getAttribute("transferProgress");
        if (progress == null) {
            model.addAttribute("destAccount", new Account());
            return "transfer_src_1";
        } else if (progress == 2) {
            return "transfer_src_2";
        } else if (progress == 3) {
//            String rand = transactionService.getRefNum();
            TransactionFundTransfer fundTransfer = (TransactionFundTransfer) httpSession.getAttribute("newTransaction");
//            fundTransfer.setRefNumber(rand);
            httpSession.setAttribute("newTransaction", fundTransfer);
            httpSession.setAttribute("transferProgress", 4);
//            model.addAttribute("rand", rand);
            return "transfer_src_3";
        } else if (progress == 4) {
            TransactionFundTransfer fundTransfer = (TransactionFundTransfer) httpSession.getAttribute("newTransaction");
            model.addAttribute("newTransaction", fundTransfer);
            return "transfer_src_4";
        }
        return "/main";
    }
    
    @GetMapping(value = "/transfercp1")
    public String doTransferCp1(HttpSession httpSession, @ModelAttribute("destAccNumber") String destAccNumber,
            Model model, RedirectAttributes ra){
        if (httpSession.getAttribute("account") == null
                ) {
            return "redirect:/welcome";
        } 
       
//        Account destAccountRetrieved = transactionService.checkRecords(destAccNumber);
//        
//        TransactionFundTransfer newTransaction = new TransactionFundTransfer();
//        newTransaction.setAccount((Account) httpSession.getAttribute("account"));
//        newTransaction.setDestAccount(destAccNumber);
//        httpSession.setAttribute("destAccount", destAccountRetrieved);
//        httpSession.setAttribute("newTransaction", newTransaction);
//        httpSession.setAttribute("transferProgress", 2);
        return "redirect:/transfer";
    }
    
    @GetMapping(value = "/transfercp2")
    public String doTransferCp2(@ModelAttribute("amount") String amount, HttpSession httpSession, Model model){
        if (httpSession.getAttribute("account") == null
                ) {
            return "redirect:/welcome";
        } 
        if (httpSession.getAttribute("newTransaction") == null) {
            return "redirect:/main";
        }
        TransactionFundTransfer fundTransfer = (TransactionFundTransfer) httpSession.getAttribute("newTransaction");
//        fundTransfer.setAmount(amount);
        httpSession.setAttribute("newTransaction", fundTransfer);
        httpSession.setAttribute("transferProgress", 3);
        return "redirect:/transfer";
    }
    
    @GetMapping(value = "/transfer/do")
    public String doTransfer(HttpSession httpSession, Model model, RedirectAttributes ra){
        
        if (httpSession.getAttribute("account") == null
                ) {
            return "redirect:/welcome";
        } 
        
        TransactionFundTransfer fundTransfer = (TransactionFundTransfer) httpSession.getAttribute("newTransaction");
        Account account = (Account) httpSession.getAttribute("account");
//        Account destAccountRetrieved = transactionService.checkRecords(fundTransfer.getDestAccount());
        BigDecimal transferAmount = new BigDecimal(0);
//        if (!validationService.genericValidation(fundTransfer.getDestAccount())
//                || destAccountRetrieved == null) {
//            ra.addFlashAttribute("error","Invalid Account");
//            return "redirect:/transfer";
//        }
//        if (!validationService.isNumeric(fundTransfer.getAmount())) {
//            ra.addFlashAttribute("error","Invalid Amount");
//            return "redirect:/transfer";
//        } else {
//            transferAmount = new BigDecimal(fundTransfer.getAmount());
//        }
        if (transferAmount.compareTo(new BigDecimal(1000)) == 1){
            ra.addFlashAttribute("error","Maximum amount to withdraw is $1000");
            return "redirect:/transfer";
        }
        if (transferAmount.compareTo(new BigDecimal(1)) == -1) {
            ra.addFlashAttribute("error","Minimum amount to withdraw is $1");
            return "redirect:/transfer";
        }
        if (fundTransfer.getRefNumber().isEmpty()) {
            ra.addFlashAttribute("error","Ref Number is Empty");
            return "redirect:/transfer";
        }
//        if (!fundTransfer.getRefNumber().isEmpty()
//                && !validationService.genericValidation(fundTransfer.getRefNumber())) {
//            ra.addFlashAttribute("error","Invalid Reference Number");
//            return "redirect:/transfer";
//        }
        if (account.getBalance().compareTo(transferAmount) == -1) {
            ra.addFlashAttribute("error","Insufficient balance for $"+transferAmount.toString());
            return "redirect:/transfer";
        }
        
//        transactionService.fundTransfer(account, fundTransfer, destAccountRetrieved);
        httpSession.setAttribute("account", account);
        
        return "redirect:/transfer/summary";
    }
    
    @GetMapping(value = "/transfer/summary")
    public String transferSummaryPage(HttpSession httpSession, Model model){
        TransactionFundTransfer fundTransfer = (TransactionFundTransfer) httpSession.getAttribute("newTransaction");
//        fundTransfer.setAmount(fundTransfer.getAmount().substring(1, fundTransfer.getAmount().length()));
        model.addAttribute("transaction", fundTransfer);
        return "transfer_summary";
    }
  
     
}
