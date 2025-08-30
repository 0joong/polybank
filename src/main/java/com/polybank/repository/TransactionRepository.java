package com.polybank.repository;

import com.polybank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // 특정 계좌의 모든 거래 내역을 찾는 메서드 (입출금 내역 조회에 사용)
    // 최신순으로 정렬하기 위해 OrderBy와 Desc 키워드를 사용
    List<Transaction> findByAccount_AccountNumberOrderByTransactionDateDesc(String accountNumber);
}