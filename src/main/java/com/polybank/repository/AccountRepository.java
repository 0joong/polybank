package com.polybank.repository;

import com.polybank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, String> {

    @Query(value = "SELECT account_num_seq.NEXTVAL FROM DUAL", nativeQuery = true)
    Long getNextAccountNumberSequence();
}