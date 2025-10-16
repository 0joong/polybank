package com.polybank.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AUTO_DEBIT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AutoDebit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_debit_seq_generator")
    @SequenceGenerator(name = "auto_debit_seq_generator", sequenceName = "auto_debit_seq", allocationSize = 1)
    @Column(name = "auto_debit_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_number", nullable = false)
    private Account fromAccount; // 출금 계좌

    @Column(name = "to_account_number", nullable = false)
    private String toAccountNumber; // 입금 계좌 번호

    @Column(name = "transfer_amount", nullable = false)
    private Long amount; // 이체 금액

    @Column(name = "transfer_day", nullable = false)
    private int transferDay; // 이체일 (매월)

    public AutoDebit(Account fromAccount, String toAccountNumber, Long amount, int transferDay) {
        this.fromAccount = fromAccount;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.transferDay = transferDay;
    }
}