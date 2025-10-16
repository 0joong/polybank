package com.polybank.repository;

import com.polybank.entity.LoanStatus;
import com.polybank.entity.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    List<LoanApplication> findByStatus(LoanStatus status);
}