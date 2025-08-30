package com.polybank.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequestDto {
    private String fromAccountNumber;
    private String toAccountNumber;
    private Long amount;
    private String password;
}