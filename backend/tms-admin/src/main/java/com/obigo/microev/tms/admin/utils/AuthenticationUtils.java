package com.obigo.microev.tms.admin.utils;

import com.obigo.microev.tms.admin.config.security.AdminDetails;
import com.obigo.microev.tms.admin.vo.CurrentAdmin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Slf4j
public class AuthenticationUtils {

    /**
     * 현재 로그인사용자 정보
     * @return
     */
    public static CurrentAdmin getCurrentAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        AdminDetails principal = null;
        try {
            principal = (AdminDetails) authentication.getPrincipal();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        if (principal == null) {
            return null;
        }

        return CurrentAdmin.builder()
                .adminSeq(principal.getAdminSeq())
                .email(principal.getEmail())
                .userName(principal.getName())
                .contact(principal.getContact())
                .position(principal.getPosition())
                .status(principal.getStatus())
                .build();
    }
}
