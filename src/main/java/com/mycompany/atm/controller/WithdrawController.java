package com.mycompany.atm.controller;

import com.mycompany.atm.custom.exception.InsufficientBalanceException;
import com.mycompany.atm.custom.exception.MaximumAmountException;
import com.mycompany.atm.custom.exception.MinimumAmountException;
import com.mycompany.atm.custom.exception.MultiplyAmountException;
import com.mycompany.atm.domain.Account;
import com.mycompany.atm.service.TransactionService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
public class WithdrawController {
    
    @Autowired
    private final TransactionService transactionService;
       
    @GetMapping(value = "/withdraw")
    public String showWithdrawOptionPage(HttpSession httpSession){
        if (httpSession.getAttribute("account") == null) {
            return "redirect:welcome";
        }
        
        httpSession.setAttribute("withdrawOther", false);
        return "withdraw";
    }
    
    @PostMapping(value = "/withdraw")
    public String showWithdrawSummaryPage(HttpSession httpSession, 
            HttpServletRequest request, Model model,
            RedirectAttributes ra){
        Account account = (Account) httpSession.getAttribute("account");
        try {
            Integer amount = Integer.parseInt(request.getParameter("amount"));
            if (httpSession.getAttribute("account") == null
                    || amount == null 
                    ) {
                return "redirect:/welcome";
            } 
        
            account = transactionService.withdraw(account, amount);
            httpSession.setAttribute("account", account);
            model.addAttribute("account", account);
            return "/withdraw_summary";
        } catch (InsufficientBalanceException e) {
            ra.addFlashAttribute("error", "Insufficient balance $"+account.getBalance().toString());
            Boolean redirect = (Boolean) httpSession.getAttribute("withdrawOther");
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
        httpSession.setAttribute("withdrawOther", true);
        return "withdraw_other";
    }
    
}
