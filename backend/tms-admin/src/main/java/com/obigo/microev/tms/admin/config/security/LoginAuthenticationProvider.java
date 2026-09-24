package com.obigo.microev.tms.admin.config.security;


import com.obigo.microev.tms.admin.exception.*;
import com.obigo.microev.tms.admin.service.LoginService;
import com.obigo.microev.tms.core.domain.entity.Admin;
import com.obigo.microev.tms.core.util.ClientIpUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;


/**
 * 로그인 인증 처리
 */
@RequiredArgsConstructor
@Slf4j
public class LoginAuthenticationProvider implements AuthenticationProvider {

    private final LoginService loginService;
    private final PasswordEncoder passwordEncoder;



    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("################## authenticate");

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = ClientIpUtils.getUserIP(request);

        String email = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UsernamePasswordAuthenticationToken authenticationToken;
        try {

            //사용자 조회
            Optional<Admin> adminOptional = loginService.getAdminByEmail(email);
            //Portal User가 없는 경우
            if (adminOptional.isEmpty()) {
                throw new InvalidUserAccountException();
            }

            Admin admin = adminOptional.get();
            AdminDetails adminDetails = new AdminDetails(admin);

            //LOCK 상태인 경우
            if (!adminDetails.isAccountNonLocked()) {
                throw new LockedUserAccountException();
            }

            //SLEEP 상태인 경우
            if (adminDetails.isDormant()) {
                throw new DormantUserAccountException();
            }

            //Normal 상태가 아닌 경우
            if (!adminDetails.isEnabled()) {
                throw new WithdrawalUserAccountException();
            }

            //비밀번호가 일치하지 않는 경우
            if (!passwordEncoder.matches(password, adminDetails.getPassword())) {

                //비밀번호 실패 처리
                int failCount = loginService.processFailedLogin(admin, ipAddress);

                //비밀번호 실패 횟수가 최대 횟수를 초과한 경우
                if (failCount >= LoginService.MAX_FAIL_COUNT) {
                    throw new LockedUserAccountException();
                }

                //비밀번호가 실패한 경우 에러
                throw new BadPasswordException(failCount);
            }

//            //최초 로그인인 경우 마지막 로그인 일시가 NULL
//            if (admin.getPasswordChangedAt() == null && admin.getLastAccessedAt() == null) {
//                throw new RequiredUpdatePasswordException();
//            }
//
//            //패스워드 변경일자가 90일을 초과
//            boolean isOver90daysOfPasswordChanged = admin.isOver90daysOfPasswordChanged();
//            if (isOver90daysOfPasswordChanged) {
//                throw new PasswordExpiredException();
//            }


            //Authentication 설정
            adminDetails.setAuthorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
            authenticationToken = new UsernamePasswordAuthenticationToken(adminDetails, password, adminDetails.getAuthorities());
            return authenticationToken;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof CustomCredentialException) {
                throw e;
            } else {
                throw new BadCredentialsException(e.getMessage());
            }
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
