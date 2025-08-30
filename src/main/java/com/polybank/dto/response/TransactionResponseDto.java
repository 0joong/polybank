package com.polybank.dto.response;

import com.polybank.entity.Transaction;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TransactionResponseDto {

    private final LocalDateTime transactionDate;
    private final String transactionType;
    private final Long amount;
    private final Long balanceAfterTransaction;
    private final String description;

    public TransactionResponseDto(Transaction transaction) {
        this.transactionDate = transaction.getTransactionDate();
        this.transactionType = transaction.getTransactionType();
        this.amount = transaction.getAmount();
        this.balanceAfterTransaction = transaction.getBalanceAfterTransaction();
        this.description = transaction.getDescription();
    }
}