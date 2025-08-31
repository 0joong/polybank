package com.polybank.controller;

import com.polybank.entity.FinancialProduct;
import com.polybank.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public String productListPage(Model model) {
        List<FinancialProduct> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "products";
    }
}