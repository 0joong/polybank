package com.polybank.dto.response;

import com.polybank.entity.Account;
import lombok.Getter;

@Getter
public class AccountResponseDto {
    private final String accountNumber;
    private final String accountType;
    private final Long balance;

    public AccountResponseDto(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.accountType = account.getAccountType();
        this.balance = account.getBalance();
    }
}