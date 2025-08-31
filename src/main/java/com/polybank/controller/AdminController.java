package com.polybank.controller;

import com.polybank.dto.request.CreateProductRequestDto;
import com.polybank.entity.FinancialProduct;
import com.polybank.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;

    @GetMapping
    public String adminMainPage(Model model) {
        List<FinancialProduct> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "admin/main";
    }

    @PostMapping("/products")
    public String createProduct(@ModelAttribute CreateProductRequestDto requestDto,
                                RedirectAttributes redirectAttributes) {

        productService.createProduct(requestDto);

        redirectAttributes.addFlashAttribute("successMessage", "새로운 금융상품이 성공적으로 등록되었습니다.");
        return "redirect:/admin";
    }

    @PostMapping("/products/{productId}/delete")
    public String deleteProduct(@PathVariable Long productId, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(productId);
        redirectAttributes.addFlashAttribute("successMessage", "금융상품이 삭제되었습니다.");
        return "redirect:/admin";
    }
}