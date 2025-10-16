package com.polybank.service;

import com.polybank.entity.LoanStatus;
import com.polybank.dto.request.LoanApplicationRequestDto;
import com.polybank.entity.LoanApplication;
import com.polybank.entity.LoanApplicationFile;
import com.polybank.entity.Member;
import com.polybank.util.FileStore;
import com.polybank.repository.LoanApplicationFileRepository;
import com.polybank.repository.LoanApplicationRepository;
import com.polybank.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanApplicationFileRepository loanApplicationFileRepository;
    private final MemberRepository memberRepository;
    private final AccountService accountService;
    private final FileStore fileStore;

    @Transactional
    public void applyForLoan(LoanApplicationRequestDto requestDto, MultipartFile documentFile, String username) throws IOException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        LoanApplication application = new LoanApplication(member, requestDto.getAmount());
        loanApplicationRepository.save(application);

        String storedFileName = fileStore.storeFile(documentFile);
        if (storedFileName != null) {
            LoanApplicationFile file = new LoanApplicationFile(application, documentFile.getOriginalFilename(), storedFileName);
            loanApplicationFileRepository.save(file);
        }
    }

    @Transactional
    public void approveLoan(Long applicationId) {
        LoanApplication application = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("신청 정보를 찾을 수 없습니다."));

        application.approve();

        accountService.createAndDisburseLoan(application.getMember(), application.getAmount());
    }

    @Transactional
    public void rejectLoan(Long applicationId) {
        // 1. 대출 신청 정보 조회
        LoanApplication application = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("신청 정보를 찾을 수 없습니다."));

        // 2. 신청 상태를 'REJECTED'로 변경
        application.reject();
    }

    // ===== 관리자 페이지용 조회 메서드들 =====
    @Transactional(readOnly = true)
    public List<LoanApplication> findAllPendingApplications() {
        return loanApplicationRepository.findByStatus(LoanStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public LoanApplication findApplicationDetails(Long applicationId) {
        return loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("신청 정보를 찾을 수 없습니다."));
    }
}