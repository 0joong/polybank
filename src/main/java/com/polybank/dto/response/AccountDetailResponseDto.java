package com.polybank.dto.response;

import com.polybank.entity.Account;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AccountDetailResponseDto {

    private final String accountNumber;
    private final String accountType;
    private final Long balance;
    private final LocalDateTime createdAt;
    private final String ownerName;

    public AccountDetailResponseDto(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.accountType = account.getAccountType();
        this.balance = account.getBalance();
        this.createdAt = account.getCreatedAt();
        this.ownerName = account.getMember().getName();
    }
}