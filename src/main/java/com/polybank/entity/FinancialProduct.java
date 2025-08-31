package com.polybank.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "FINANCIAL_PRODUCT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FinancialProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_generator")
    @SequenceGenerator(name = "product_seq_generator", sequenceName = "product_seq", allocationSize = 1)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", nullable = false, unique = true, length = 100)
    private String productName;

    @Column(name = "product_type", nullable = false, length = 20)
    private String productType;

    @Column(name = "interest_rate", nullable = false)
    private Double interestRate;

    @Column(name = "description", length = 1000)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public FinancialProduct(String productName, String productType, Double interestRate, String description) {
        this.productName = productName;
        this.productType = productType;
        this.interestRate = interestRate;
        this.description = description;
    }
}