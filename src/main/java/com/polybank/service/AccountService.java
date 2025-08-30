package com.polybank.service;

import com.polybank.dto.request.CreateAccountRequestDto;
import com.polybank.dto.request.TransferRequestDto;
import com.polybank.dto.response.AccountResponseDto;
import com.polybank.entity.Account;
import com.polybank.entity.Member;
import com.polybank.entity.Transaction;
import com.polybank.repository.AccountRepository;
import com.polybank.repository.MemberRepository;
import com.polybank.repository.TransactionRepository;
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
    private final TransactionRepository transactionRepository;
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

    @Transactional
    public void executeTransfer(TransferRequestDto requestDto, String username) {
        // 출금 계좌와 입금 계좌 조회
        Account fromAccount = accountRepository.findById(requestDto.getFromAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("출금 계좌를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findById(requestDto.getToAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("입금 계좌를 찾을 수 없습니다."));

        // 유효성 검증
        // 출금 계좌 소유주가 현재 로그인한 사용자인지 확인
        if (!fromAccount.getMember().getUsername().equals(username)) {
            throw new IllegalStateException("본인 소유의 계좌에서만 출금할 수 있습니다.");
        }
        // 계좌 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), fromAccount.getPassword())) {
            throw new IllegalArgumentException("계좌 비밀번호가 일치하지 않습니다.");
        }
        // 잔액 확인
        if (fromAccount.getBalance() < requestDto.getAmount()) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }

        // 이체 로직
        fromAccount.withdraw(requestDto.getAmount());
        toAccount.deposit(requestDto.getAmount());

        // 거래 내역 기록 (출금/입금)
        Transaction withdrawalTransaction = new Transaction(fromAccount, "TRANSFER_OUT", requestDto.getAmount(), fromAccount.getBalance(), toAccount.getAccountNumber());
        Transaction depositTransaction = new Transaction(toAccount, "TRANSFER_IN", requestDto.getAmount(), toAccount.getBalance(), fromAccount.getAccountNumber());

        transactionRepository.save(withdrawalTransaction);
        transactionRepository.save(depositTransaction);
    }
}