package com.polybank.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LOAN_APPLICATION_FILE")
@Getter
@NoArgsConstructor
public class LoanApplicationFile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_app_file_seq_generator")
    @SequenceGenerator(name = "loan_app_file_seq_generator", sequenceName = "loan_app_file_seq", allocationSize = 1)
    @Column(name = "file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private LoanApplication loanApplication;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "stored_filepath", nullable = false)
    private String storedFilepath;

    public LoanApplicationFile(LoanApplication loanApplication, String originalFilename, String storedFilepath) {
        this.loanApplication = loanApplication;
        this.originalFilename = originalFilename;
        this.storedFilepath = storedFilepath;
    }
}