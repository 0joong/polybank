package com.polybank.service;

import com.polybank.entity.Account;
import com.polybank.entity.AutoDebit;
import com.polybank.entity.Transaction;
import com.polybank.repository.AccountRepository;
import com.polybank.repository.AutoDebitRepository;
import com.polybank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoDebitService {

    private final AutoDebitRepository autoDebitRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * 오늘 날짜에 해당하는 모든 자동이체 처리
     */
    @Transactional
    public void processAutoDebitsForToday() {
        int today = LocalDate.now().getDayOfMonth();
        log.info(">> 자동이체 스케줄러 시작 (오늘 날짜: {}일)", today);

        // 1. 오늘 실행해야 할 자동이체 목록 조회
        List<AutoDebit> scheduledDebits = autoDebitRepository.findByTransferDay(today);
        if (scheduledDebits.isEmpty()) {
            log.info(">> 오늘 실행할 자동이체 항목이 없습니다.");
            return;
        }

        // 2. 각 항목에 대해 이체 실행
        for (AutoDebit debit : scheduledDebits) {
            try {
                processSingleDebit(debit);
            } catch (Exception e) {
                // 개별 이체 실패 시 로그만 남기고 다음 이체를 계속 진행
                log.error(">> 자동이체 실패 (ID: {}): {}", debit.getId(), e.getMessage());
            }
        }
        log.info(">> 자동이체 스케줄러 종료");
    }

    /**
     * 단일 자동이체 건 처리
     */
    private void processSingleDebit(AutoDebit debit) {
        Account fromAccount = debit.getFromAccount();
        Account toAccount = accountRepository.findById(debit.getToAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("입금 계좌를 찾을 수 없습니다."));

        Long amount = debit.getAmount();

        // 잔액 확인
        if (fromAccount.getBalance() < amount) {
            throw new IllegalStateException("잔액이 부족합니다. (계좌: " + fromAccount.getAccountNumber() + ")");
        }

        // 이체 실행
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        // 거래 내역 기록
        Transaction withdrawalTx = new Transaction(fromAccount, "AUTO_DEBIT_OUT", amount, fromAccount.getBalance(), "자동이체 -> " + toAccount.getAccountNumber());
        Transaction depositTx = new Transaction(toAccount, "AUTO_DEBIT_IN", amount, toAccount.getBalance(), "자동이체 <- " + fromAccount.getAccountNumber());

        transactionRepository.save(withdrawalTx);
        transactionRepository.save(depositTx);

        log.info(">> 자동이체 성공: {} -> {} (금액: {})", fromAccount.getAccountNumber(), toAccount.getAccountNumber(), amount);
    }
}