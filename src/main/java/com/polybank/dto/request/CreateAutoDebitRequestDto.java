package com.polybank.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAutoDebitRequestDto {
    private String toAccountNumber;
    private Long amount;
    private int transferDay;
    private String password;
}