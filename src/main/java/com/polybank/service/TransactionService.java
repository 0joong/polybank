package com.polybank.service;

import com.polybank.dto.response.TransactionResponseDto;
import com.polybank.entity.Account;
import com.polybank.entity.Transaction;
import com.polybank.repository.AccountRepository;
import com.polybank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> findTransactionsByAccountNumber(String accountNumber, String username) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));

        if (!account.getMember().getUsername().equals(username)) {
            throw new IllegalStateException("본인 소유의 계좌만 조회할 수 있습니다.");
        }

        List<Transaction> transactions = transactionRepository.findByAccount_AccountNumberOrderByTransactionDateDesc(accountNumber);

        return transactions.stream()
                .map(TransactionResponseDto::new)
                .collect(Collectors.toList());
    }
}