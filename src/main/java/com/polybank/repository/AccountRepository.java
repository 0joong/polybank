package com.polybank.repository;

import com.polybank.entity.Account;
import com.polybank.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {

    @Query(value = "SELECT account_num_seq.NEXTVAL FROM DUAL", nativeQuery = true)
    Long getNextAccountNumberSequence();

    List<Account> findByMember(Member member);

    List<Account> findByMemberAndAccountStatus(Member member, String status);

    // 만기일, 계좌상태, 계좌종류(type)를 모두 조건으로 조회하도록 수정
    List<Account> findByMaturityDateAndAccountStatusAndAccountType(LocalDate maturityDate, String status, String accountType);
}