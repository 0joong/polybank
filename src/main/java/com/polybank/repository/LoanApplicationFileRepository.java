package com.polybank.repository;

import com.polybank.entity.LoanApplicationFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanApplicationFileRepository extends JpaRepository<LoanApplicationFile, Long> {

}