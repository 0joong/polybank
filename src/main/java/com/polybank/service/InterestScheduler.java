package com.polybank.service;

import com.polybank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterestScheduler {

    private final AccountRepository accountRepository;
    private final InterestService interestService;

    // 매일 00시 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void calculateOverdraftInterest() {
        System.out.println("이자 계산 스케줄러 시작");

        interestService.payInterestForMaturedAccounts();

        System.out.println("이자 계산 스케줄러 종료");
    }
}