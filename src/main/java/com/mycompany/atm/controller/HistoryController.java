package com.mycompany.atm.controller;

import com.mycompany.atm.domain.Account;
import com.mycompany.atm.service.TransactionService;
import java.time.LocalDate;
import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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
public class HistoryController {
    
    @Autowired
    private final TransactionService transactionService;
           
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
