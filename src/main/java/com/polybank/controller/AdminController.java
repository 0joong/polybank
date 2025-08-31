package com.polybank.controller;

import com.polybank.dto.request.CreateProductRequestDto;
import com.polybank.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;

    @GetMapping
    public String adminMainPage() {
        return "admin/main";
    }

    @PostMapping("/products")
    public String createProduct(@ModelAttribute CreateProductRequestDto requestDto,
                                RedirectAttributes redirectAttributes) {

        productService.createProduct(requestDto);

        redirectAttributes.addFlashAttribute("successMessage", "새로운 금융상품이 성공적으로 등록되었습니다.");
        return "redirect:/admin";
    }
}