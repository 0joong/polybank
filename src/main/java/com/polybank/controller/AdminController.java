package com.polybank.controller;

import com.polybank.dto.request.CreateProductRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String adminMainPage() {
        return "admin/main";
    }

    @PostMapping("/products")
    public String createProduct(@ModelAttribute CreateProductRequestDto requestDto,
                                RedirectAttributes redirectAttributes) {

        // TODO: ProductService를 만들어 실제 DB에 상품을 저장하는 로직 구현 필요
        System.out.println("새로운 상품 등록 요청: " + requestDto.getProductName());

        redirectAttributes.addFlashAttribute("successMessage", "새로운 금융상품이 성공적으로 등록되었습니다.");
        return "redirect:/admin";
    }
}