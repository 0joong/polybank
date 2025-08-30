package com.polybank.controller;

import com.polybank.dto.request.CreateAccountRequestDto;
import com.polybank.dto.response.AccountDetailResponseDto;
import com.polybank.dto.response.AccountResponseDto;
import com.polybank.repository.AccountRepository;
import com.polybank.repository.MemberRepository;
import com.polybank.repository.TransactionRepository;
import com.polybank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

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

    @GetMapping
    public String myAccountPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<AccountResponseDto> myAccounts = accountService.findMyAccounts(username);
        model.addAttribute("accounts", myAccounts);

        return "accounts";
    }

    // 아래 GetMapping 메서드를 추가해주세요.
    @GetMapping("/{accountNumber}")
    public String accountDetailPage(@PathVariable String accountNumber,
                                    Model model,
                                    @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        AccountDetailResponseDto accountDetails = accountService.findAccountDetails(accountNumber, username);

        model.addAttribute("account", accountDetails);

        return "account-details";
    }
}
