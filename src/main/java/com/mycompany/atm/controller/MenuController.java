package com.mycompany.atm.controller;

import com.mycompany.atm.custom.exception.AccountNumberException;
import com.mycompany.atm.custom.exception.InsufficientBalanceException;
import com.mycompany.atm.custom.exception.InvalidAccountException;
import com.mycompany.atm.custom.exception.InvalidAmountException;
import com.mycompany.atm.custom.exception.InvalidReferenceException;
import com.mycompany.atm.custom.exception.MaximumAmountException;
import com.mycompany.atm.custom.exception.MinimumAmountException;
import com.mycompany.atm.custom.exception.MultiplyAmountException;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.domain.TransactionFundTransfer;
import com.mycompany.atm.service.TransactionService;
import com.mycompany.atm.service.ValidationService;
import com.mycompany.atm.validator.AccountFormValidator;
import java.time.LocalDate;
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
        Account accountRetrieved;
        
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.account", bindingResult);
            ra.addFlashAttribute("account", account);
            return new ModelAndView("redirect:welcome");
        }
       
        try {
            accountRetrieved = transactionService.getAccount(account.getAccountNumber(), account.getPin());
        } catch (InvalidAccountException e){
            ra.addFlashAttribute("accountError", e.getMessage());
            return new ModelAndView("redirect:welcome") ;
        }
        httpSession.setAttribute("account", accountRetrieved);
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
        
        Account account = (Account) httpSession.getAttribute("account");
        try {
            validationService.amountValidation(amount);
            transactionService.withdraw(account, amount);
            httpSession.setAttribute("account", account);
            model.addAttribute("account", account);
            return "/withdraw_summary";
        } catch (InsufficientBalanceException e) {
            ra.addFlashAttribute("error", "Insufficient balance $"+account.getBalance().toString());
            Boolean redirect = (Boolean) httpSession.getAttribute("withdratOther");
            return redirect ? "redirect:/withdraw/other" : "redirect:/withdraw";
        } catch (MultiplyAmountException | MaximumAmountException | MinimumAmountException e) {
            ra.addFlashAttribute("error", e.getMessage());
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
            return "fund_transfer_account_input";
        } else if (progress == 2) {
            return "fund_transfer_amount_input";
        } else if (progress == 3) {
            String rand = transactionService.getRandomRefNum();
            httpSession.setAttribute("transferProgress", 4);
            httpSession.setAttribute("fund_transfer_refNumber", rand);
            model.addAttribute("rand", rand);
            return "fund_transfer_ref_number";
        } else if (progress == 4) {
            TransactionFundTransfer newTransaction = new TransactionFundTransfer();
            newTransaction.setDestAccount((String) httpSession.getAttribute("fund_transfer_destAccNumber"));
            newTransaction.setAmount(Integer.valueOf((String) httpSession.getAttribute("fund_transfer_amount")));
            newTransaction.setRefNumber((String)httpSession.getAttribute("fund_transfer_refNumber"));
            httpSession.setAttribute("newTransaction", newTransaction);
            model.addAttribute("newTransaction", newTransaction);
            return "fund_transfer_confirmation";
        }
        return "/main";
    }
    
    @GetMapping(value = "/fund_transfer_save_data")
    public String transferDestAccountCheckpoint(HttpSession httpSession, @ModelAttribute("destAccNumber") String destAccNumber, @ModelAttribute("amount") String amount,
            Model model, RedirectAttributes ra){
        if (httpSession.getAttribute("account") == null
                ) {
            return "redirect:/welcome";
        } 
        if (!destAccNumber.equals("")) {
            httpSession.setAttribute("fund_transfer_destAccNumber", destAccNumber);
            httpSession.setAttribute("transferProgress", 2);
        }
        if (!amount.equals("")) {
            httpSession.setAttribute("fund_transfer_amount", amount);
            httpSession.setAttribute("transferProgress", 3);
        }
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
        
        try {
            validationService.credentialsValidation("AccountNumber", fundTransfer.getDestAccount());
            transactionService.checkAccountAvailability(fundTransfer.getDestAccount());
            validationService.amountValidation(fundTransfer.getAmount());
            validationService.checkRefNumber(fundTransfer.getRefNumber());
            transactionService.fundTransfer(account, fundTransfer);
            httpSession.setAttribute("account", account);
        } catch (AccountNumberException | InvalidAccountException | InvalidAmountException | MultiplyAmountException | MaximumAmountException | MinimumAmountException |
                InvalidReferenceException | InsufficientBalanceException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/transfer";
        }       
        
        return "redirect:/transfer/summary";
    }
    
    @GetMapping(value = "/transfer/summary")
    public String transferSummaryPage(HttpSession httpSession, Model model){
        
        if (httpSession.getAttribute("newTransaction") == null
                ) {
            return "redirect:/welcome";
        } 
        
        TransactionFundTransfer fundTransfer = (TransactionFundTransfer) httpSession.getAttribute("newTransaction");
        fundTransfer.setAmount(fundTransfer.getAmount());
        model.addAttribute("transaction", fundTransfer);
        return "transfer_summary";
    }
    
    @GetMapping(value = "/history")
    public String historyPage(HttpSession httpSession, Model model, @ModelAttribute("filterDate") String filterDate){
        
        if (httpSession.getAttribute("account") == null
                ) {
            return "redirect:/welcome";
        } 
        final LocalDate compareDate = !filterDate.equals("") ? LocalDate.parse(filterDate) : LocalDate.now();
        model.addAttribute("transHistory", transactionService.getLast10Transaction((Account) httpSession.getAttribute("account"), compareDate));
        model.addAttribute("filterDate", compareDate.toString());
        return "trans_history";
    }
     
}
