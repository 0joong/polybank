package com.polybank.repository;

import com.polybank.entity.AutoDebit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutoDebitRepository extends JpaRepository<AutoDebit, Long> {

    /**
     * 특정 이체일에 해당하는 모든 자동이체 정보를 조회
     * @param transferDay 매월 이체일 (1-28)
     * @return 자동이체 목록
     */
    List<AutoDebit> findByTransferDay(int transferDay);

    List<AutoDebit> findByFromAccount_AccountNumber(String accountNumber);
}