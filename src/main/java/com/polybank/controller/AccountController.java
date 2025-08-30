package com.polybank.controller;

import com.polybank.dto.request.CreateAccountRequestDto;
import com.polybank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public String myAccountPage() {
        return "accounts";
    }

    // 아래 메서드를 추가해주세요.
    @GetMapping("/new")
    public String createAccountForm() {
        return "create-account-form";
    }

    @PostMapping
    public String createAccount(@ModelAttribute CreateAccountRequestDto requestDto,
                                @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();

        accountService.createAccount(requestDto, username);

        return "redirect:/accounts";
    }
}
