package com.polybank.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositWithdrawRequestDto {
    private String accountNumber;
    private Long amount;
    private String password;
}