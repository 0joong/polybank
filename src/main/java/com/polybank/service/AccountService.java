package com.polybank.service;

import com.polybank.dto.request.CreateAccountRequestDto;
import com.polybank.dto.request.DepositWithdrawRequestDto;
import com.polybank.dto.request.TransferRequestDto;
import com.polybank.dto.response.AccountDetailResponseDto;
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

    @Transactional(readOnly = true)
    public AccountDetailResponseDto findAccountDetails(String accountNumber, String username) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));

        if (!account.getMember().getUsername().equals(username)) {
            throw new IllegalStateException("본인 소유의 계좌만 조회할 수 있습니다.");
        }

        return new AccountDetailResponseDto(account);
    }

    @Transactional
    public void createAccount(CreateAccountRequestDto requestDto, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 상품 타입 문자열을 두 자리 코드로 변환(헬퍼 메서드 이용)
        String productCode = getProductCodeByType(requestDto.getAccountType());

        Long nextSeq = accountRepository.getNextAccountNumberSequence();

        // 변환된 상품 코드를 사용하여 계좌번호를 생성(예: 12-000001)
        String newAccountNumber = String.format("%s-%06d", productCode, nextSeq);

        Account newAccount = new Account();
        newAccount.setAccountNumber(newAccountNumber);
        newAccount.setMember(member);
        newAccount.setAccountType(productCode);
        newAccount.setPassword(encodedPassword);
        newAccount.setBalance(0L);

        accountRepository.save(newAccount);
    }

    /**
     * 상품 타입 문자열(DEPOSIT, SAVINGS 등)을 두 자리 상품 코드로 변환하는 헬퍼 메서드
     */
    private String getProductCodeByType(String productType) {
        return switch (productType) {
            case "DEPOSIT" -> "12"; // 예금
            case "SAVINGS" -> "11"; // 적금
            default -> "10";      // 자유입출금 등 기본값
        };
    }

    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    public List<AccountResponseDto> findMyAccounts(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Account> myAccounts = accountRepository.findByMemberAndAccountStatus(member, "ACTIVE");

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
    @Transactional
    public void closeAccount(String accountNumber, String username) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));

        if (!account.getMember().getUsername().equals(username)) {
            throw new IllegalStateException("본인 소유의 계좌만 해지할 수 있습니다.");
        }

        if (account.getBalance() != 0) {
            throw new IllegalStateException("잔액이 0원인 계좌만 해지할 수 있습니다. 잔액을 모두 이체해주세요.");
        }

        account.setAccountStatus("CLOSED");
    }

    @Transactional
    public void deposit(DepositWithdrawRequestDto requestDto) {
        Account account = accountRepository.findById(requestDto.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));

        // 입금 처리
        account.deposit(requestDto.getAmount());

        // 거래내역 기록
        Transaction transaction = new Transaction(account, "DEPOSIT", requestDto.getAmount(), account.getBalance(), "계좌 입금");
        transactionRepository.save(transaction);
    }

    @Transactional
    public void withdraw(DepositWithdrawRequestDto requestDto, String username) {
        Account account = accountRepository.findById(requestDto.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));

        if (!account.getMember().getUsername().equals(username)) {
            throw new IllegalStateException("본인 소유의 계좌에서만 출금할 수 있습니다.");
        }
        if (!passwordEncoder.matches(requestDto.getPassword(), account.getPassword())) {
            throw new IllegalArgumentException("계좌 비밀번호가 일치하지 않습니다.");
        }

        // 출금 처리
        account.withdraw(requestDto.getAmount());

        // 거래내역 기록
        Transaction transaction = new Transaction(account, "WITHDRAW", requestDto.getAmount(), account.getBalance(), "계좌 출금");
        transactionRepository.save(transaction);
    }

}