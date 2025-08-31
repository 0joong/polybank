package com.polybank.controller;

import com.polybank.dto.request.CreateAccountRequestDto;
import com.polybank.dto.request.DepositWithdrawRequestDto;
import com.polybank.dto.response.AccountDetailResponseDto;
import com.polybank.dto.response.AccountResponseDto;
import com.polybank.entity.FinancialProduct;
import com.polybank.repository.AccountRepository;
import com.polybank.repository.FinancialProductRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final FinancialProductRepository financialProductRepository;

    @GetMapping("/new")
    public String createAccountForm(@RequestParam Long productId, Model model) {
        FinancialProduct product = financialProductRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 정보를 찾을 수 없습니다."));

        model.addAttribute("product", product);

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

    @GetMapping("/{accountNumber}")
    public String accountDetailPage(@PathVariable String accountNumber,
                                    Model model,
                                    @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        AccountDetailResponseDto accountDetails = accountService.findAccountDetails(accountNumber, username);

        model.addAttribute("account", accountDetails);

        return "account-details";
    }

    @PostMapping("/{accountNumber}/delete")
    public String closeAccount(@PathVariable String accountNumber,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        try {
            accountService.closeAccount(accountNumber, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("successMessage", "계좌가 정상적으로 해지되었습니다.");
            return "redirect:/accounts";

        } catch (IllegalStateException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/accounts/" + accountNumber;
        }
    }

    @PostMapping("/{accountNumber}/deposit")
    public String deposit(@PathVariable String accountNumber,
                          @ModelAttribute DepositWithdrawRequestDto requestDto,
                          RedirectAttributes redirectAttributes) {

        requestDto.setAccountNumber(accountNumber);
        accountService.deposit(requestDto);

        redirectAttributes.addFlashAttribute("successMessage", "입금이 완료되었습니다.");
        return "redirect:/accounts/" + accountNumber;
    }

    @PostMapping("/{accountNumber}/withdraw")
    public String withdraw(@PathVariable String accountNumber,
                           @ModelAttribute DepositWithdrawRequestDto requestDto,
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {

        requestDto.setAccountNumber(accountNumber);
        accountService.withdraw(requestDto, userDetails.getUsername());

        redirectAttributes.addFlashAttribute("successMessage", "출금이 완료되었습니다.");
        return "redirect:/accounts/" + accountNumber;
    }
}
