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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class FundTransferController {
    
    @Autowired
    private final TransactionService transactionService;
   
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
            TransactionFundTransfer newTransaction = transactionService.setNewFundTransaction(httpSession);
            httpSession.setAttribute("newTransaction", newTransaction);
            model.addAttribute("newTransaction", newTransaction);
            return "fund_transfer_confirmation";
        }
        return "/main";
    }
    
    @PostMapping(value = "/fund_transfer_save_data")
    public String transferDestAccountCheckpoint(HttpSession httpSession, HttpServletRequest request, 
            RedirectAttributes ra){
        String destAccNumber = request.getParameter("accountNumber");
        String amount = request.getParameter("amount");
        
        if (httpSession.getAttribute("account") == null
                ) {
            return "redirect:/welcome";
        } 
        if (destAccNumber != null) {
            httpSession.setAttribute("fund_transfer_destAccNumber", destAccNumber);
            httpSession.setAttribute("transferProgress", 2);
        }
        if (amount != null) {
            httpSession.setAttribute("fund_transfer_amount", amount);
            httpSession.setAttribute("transferProgress", 3);
        }
        return "redirect:/transfer";
    }
        
    @PostMapping(value = "/transfer/do")
    public String doTransfer(HttpSession httpSession, RedirectAttributes ra){
        
        try {
        
            if (httpSession.getAttribute("account") == null
                    ) {
                return "redirect:/welcome";
            } 

            TransactionFundTransfer fundTransfer = (TransactionFundTransfer) httpSession.getAttribute("newTransaction");
            Account account = (Account) httpSession.getAttribute("account");
            account = transactionService.fundTransfer(account, fundTransfer);
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
     
}
