package com.polybank.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ACCOUNT")
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @Column(name = "account_number", length = 9)
    private String accountNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "account_type", nullable = false, length = 2)
    private String accountType;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "balance", nullable = false)
    private Long balance;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "account_status", nullable = false)
    private String accountStatus;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();

        if (this.accountStatus == null) {
            this.accountStatus = "ACTIVE";
        }
    }

    /**
     * 입금 기능: 현재 잔액에 입금액을 더합니다.
     * @param amount 입금할 금액
     */
    public void deposit(Long amount) {
        this.balance += amount;
    }

    /**
     * 출금 기능: 현재 잔액에서 출금액을 차감합니다.
     * @param amount 출금할 금액
     */
    public void withdraw(Long amount) {
        if (this.balance < amount) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }
        this.balance -= amount;
    }
}