package com.polybank.service;

import com.polybank.dto.request.CreateProductRequestDto;
import com.polybank.entity.FinancialProduct;
import com.polybank.repository.FinancialProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final FinancialProductRepository productRepository;

    @Transactional
    public void createProduct(CreateProductRequestDto requestDto) {
        FinancialProduct product = new FinancialProduct(
                requestDto.getProductName(),
                requestDto.getProductType(),
                requestDto.getInterestRate(),
                requestDto.getDescription()
        );
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<FinancialProduct> findAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}