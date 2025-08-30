package com.polybank.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ACCOUNT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @Column(name = "account_number", length = 9)
    private String accountNumber; // 1. DB 트리거로 생성되므로 @GeneratedValue 없음

    @ManyToOne(fetch = FetchType.LAZY) // 2. Member 엔티티와 다대일 관계
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

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}