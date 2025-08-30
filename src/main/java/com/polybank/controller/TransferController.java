package com.polybank.controller;

import com.polybank.dto.request.TransferRequestDto;
import com.polybank.dto.response.AccountResponseDto;
import com.polybank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransferController {

    private final AccountService accountService;

    @GetMapping("/transfer")
    public String transferForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<AccountResponseDto> myAccounts = accountService.findMyAccounts(userDetails.getUsername());
        model.addAttribute("myAccounts", myAccounts);
        return "transfer";
    }

    @PostMapping("/transfer")
    public String executeTransfer(@ModelAttribute TransferRequestDto requestDto,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            accountService.executeTransfer(requestDto, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("successMessage", "이체가 성공적으로 완료되었습니다.");
            return "redirect:/accounts";
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            List<AccountResponseDto> myAccounts = accountService.findMyAccounts(userDetails.getUsername());
            model.addAttribute("myAccounts", myAccounts);

            return "transfer";
        }
    }
}