package com.polybank.service;

import com.polybank.dto.request.CreateAccountRequestDto;
import com.polybank.dto.response.AccountResponseDto;
import com.polybank.entity.Account;
import com.polybank.entity.Member;
import com.polybank.repository.AccountRepository;
import com.polybank.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createAccount(CreateAccountRequestDto requestDto, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        Long nextSeq = accountRepository.getNextAccountNumberSequence();

        // 계좌번호 직접 생성 (예: 10-000001)
        String newAccountNumber = String.format("%s-%06d", requestDto.getAccountType(), nextSeq);

        Account newAccount = new Account();
        newAccount.setAccountNumber(newAccountNumber);
        newAccount.setMember(member);
        newAccount.setAccountType(requestDto.getAccountType());
        newAccount.setPassword(encodedPassword);
        newAccount.setBalance(0L);

        accountRepository.save(newAccount);
    }

    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    public List<AccountResponseDto> findMyAccounts(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Account> myAccounts = accountRepository.findByMember(member);

        return myAccounts.stream()
                .map(AccountResponseDto::new)
                .collect(Collectors.toList());
    }
}