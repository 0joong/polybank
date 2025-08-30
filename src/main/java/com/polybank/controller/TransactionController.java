package com.polybank.controller;

import com.polybank.dto.response.TransactionResponseDto;
import com.polybank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/transactions")
    public String getTransactionHistoryPage(@RequestParam String accountNumber,
                                            Model model,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        List<TransactionResponseDto> transactions = transactionService.findTransactionsByAccountNumber(accountNumber, username);

        model.addAttribute("transactions", transactions);
        model.addAttribute("accountNumber", accountNumber);

        return "transactions";
    }
}