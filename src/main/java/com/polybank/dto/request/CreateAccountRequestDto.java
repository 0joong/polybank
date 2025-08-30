package com.polybank.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequestDto {
    private String accountType;
    private String password;
}