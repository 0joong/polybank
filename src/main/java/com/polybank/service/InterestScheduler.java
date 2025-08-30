package com.polybank.service;

import com.polybank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterestScheduler {

    private final AccountRepository accountRepository;

    // 매일 00시 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void calculateOverdraftInterest() {
        System.out.println("이자 계산 스케줄러 시작");

        // TODO 이자 계산 로직 구현
        // accountType이 'OVERDRAFT'이고 잔액이 마이너스인 모든 계좌를 조회
        // 각 계좌에 대해 일일 이자를 계산하여 이자 총액에 누적
        // 매달 말일에 이자 총액을 잔액에서 차감하는 로직 구현

        System.out.println("이자 계산 스케줄러 종료");
    }
}