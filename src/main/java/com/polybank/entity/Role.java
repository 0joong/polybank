package com.polybank.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "개인"),
    ADMIN("ROLE_ADMIN", "관리자");
    // 추후 CORPORATE("ROLE_CORPORATE", "기업 회원") 등 추가 가능

    private final String key;
    private final String title;
}