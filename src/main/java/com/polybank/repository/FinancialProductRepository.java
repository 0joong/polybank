package com.polybank.repository;

import com.polybank.entity.FinancialProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialProductRepository extends JpaRepository<FinancialProduct, Long> {
}