package com.polybank.service;

import com.polybank.entity.Account;
import com.polybank.entity.Transaction;
import com.polybank.repository.AccountRepository;
import com.polybank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void payInterestForMaturedAccounts() {
        // 오늘 날짜에 만기되는 ACTIVE, 예금(DEPOSIT) 계좌만 찾음
        List<Account> maturedDepositAccounts = accountRepository.findByMaturityDateAndAccountStatusAndAccountType(
                LocalDate.now(), "ACTIVE", "12" // '12'는 예금을 의미
        );

        for (Account account : maturedDepositAccounts) {
            // 예치 기간(일) 계산
            long daysHeld = ChronoUnit.DAYS.between(account.getCreatedAt().toLocalDate(), account.getMaturityDate());
            double annualInterestRate = account.getInterestRate() / 100.0;

            // 예금 이자 계산(원금 * 연이율 * 예치일수 / 365)
            long interestAmount = (long) (account.getBalance() * annualInterestRate * daysHeld / 365.0);

            // 이자 입금
            account.deposit(interestAmount);

            // 거래내역 기록
            Transaction interestTransaction = new Transaction(account, "INTEREST", interestAmount, account.getBalance(), "정기예금 만기 이자");
            transactionRepository.save(interestTransaction);

            // 계좌 상태 만기로 변경
            account.setAccountStatus("MATURED");
        }
    }
}