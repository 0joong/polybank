package com.polybank.dto.response;

import com.polybank.entity.AutoDebit;
import lombok.Getter;

@Getter
public class AutoDebitResponseDto {
    private final Long id;
    private final String toAccountNumber;
    private final Long amount;
    private final int transferDay;

    public AutoDebitResponseDto(AutoDebit autoDebit) {
        this.id = autoDebit.getId();
        this.toAccountNumber = autoDebit.getToAccountNumber();
        this.amount = autoDebit.getAmount();
        this.transferDay = autoDebit.getTransferDay();
    }
}