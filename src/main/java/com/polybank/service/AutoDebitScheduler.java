package com.polybank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoDebitScheduler {

    private final AutoDebitService autoDebitService;

    /**
     * 매일 새벽 2시에 자동이체 로직을 실행
     * cron = "초 분 시 일 월 요일"
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void executeDailyAutoDebits() {
        autoDebitService.processAutoDebitsForToday();
    }
}