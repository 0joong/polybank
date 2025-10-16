package com.polybank.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LOAN_APPLICATION")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication {

    public LoanApplication(Member member, Long amount) {
        this.member = member;
        this.amount = amount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_app_seq_generator")
    @SequenceGenerator(name = "loan_app_seq_generator", sequenceName = "loan_app_seq", allocationSize = 1)
    @Column(name = "application_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LoanStatus status;

    @CreationTimestamp
    @Column(name = "application_date", nullable = false, updatable = false)
    private LocalDateTime applicationDate;

    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanApplicationFile> files = new ArrayList<>();

    public void reject() {
        this.status = LoanStatus.REJECTED;
    }

    public void approve() {
        this.status = LoanStatus.APPROVED;
    }

    @PrePersist
    public void onPrePersist() {
        if (this.status == null) {
            this.status = LoanStatus.PENDING;
        }
    }
}