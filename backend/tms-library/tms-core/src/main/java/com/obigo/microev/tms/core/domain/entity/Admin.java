package com.obigo.microev.tms.core.domain.entity;

import com.obigo.microev.tms.core.domain.enumeration.AdminStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Admin {

    private Long adminSeq;
    private String email;
    private String adminName;
    private String password;
    private String contact;
    private String position;
    private int failCount;
    private LocalDateTime passwordChangedAt;
    private LocalDateTime lastAccessedAt;
    private String refreshToken;
    private String statusCd;
    private LocalDateTime createdAt;
    private Long creatorSeq;
    private LocalDateTime modifiedAt;
    private Long modifierSeq;

    /**
     * 로그인 실패 횟수 증가
     * @return
     */
    public int addFailCount() {
        this.failCount++;
        this.modifiedAt = LocalDateTime.now();
        this.modifierSeq = this.adminSeq;

        return this.failCount;
    }


    /**
     * 로그인 실패 횟수 초기화
     */
    public void resetFailCount() {
        this.failCount = 0;
        this.modifiedAt = LocalDateTime.now();
        this.modifierSeq = this.adminSeq;
    }


    /**
     * 상태 변경
     * @param adminStatus
     */
    public void changeStatus(AdminStatus adminStatus) {
        this.statusCd = adminStatus.name();
        this.modifiedAt = LocalDateTime.now();
        this.modifierSeq = this.adminSeq;
    }


    /**
     * 마지막 접속일시 변경
     */
    public void changeLastAccessedAt(String refreshToken) {
        LocalDateTime now = LocalDateTime.now();
        this.lastAccessedAt = now;
        this.refreshToken = refreshToken;
        this.modifiedAt = now;
        this.modifierSeq = this.adminSeq;
    }


    /**
     * 비밀번호 변경일로부터 90일이 지났는지 확인
     * @return
     */
    public boolean isOver90daysOfPasswordChanged() {
        LocalDateTime now = LocalDateTime.now();
        return passwordChangedAt.plusDays(90).isBefore(now);
    }

    /**
     * 비밀번호 변경
     * @param newPassword
     */
    public void changePassword(String newPassword) {
        LocalDateTime now = LocalDateTime.now();
        this.password = newPassword;
        this.passwordChangedAt = now;
        this.modifiedAt = now;
        this.modifierSeq = this.adminSeq;
    }

    public void changeRefreshToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        this.modifiedAt = LocalDateTime.now();
        this.modifierSeq = this.adminSeq;
    }
}
