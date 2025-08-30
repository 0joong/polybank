package com.polybank.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "INTEREST_RATE_HISTORY")
@Getter
@NoArgsConstructor
public class InterestRateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rate_history_seq_generator")
    @SequenceGenerator(name = "rate_history_seq_generator", sequenceName = "rate_history_seq", allocationSize = 1)
    @Column(name = "history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number")
    private Account account;

    @Column(name = "interest_rate", nullable = false)
    private Double interestRate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    public InterestRateHistory(Account account, Double interestRate, LocalDate startDate) {
        this.account = account;
        this.interestRate = interestRate;
        this.startDate = startDate;
    }
}