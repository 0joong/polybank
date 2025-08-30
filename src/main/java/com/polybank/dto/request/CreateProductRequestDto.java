package com.polybank.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequestDto {
    private String productName;
    private String productType;
    private Double interestRate;
    private String description;
}