package com.obigo.microev.tms.admin.config.security.jwt;


import com.obigo.microev.tms.admin.config.security.AdminDetails;
import com.obigo.microev.tms.core.domain.entity.Admin;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.domain.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminDetailsServiceImpl implements UserDetailsService {
    private final AdminMapper adminMapp;


    /**
     * username = email
     * @param username
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        Admin admin = adminMapp.findByEmail(username)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", username);
                    return new BadCredentialsException(ResponseCode.INVALID_ACCOUNT_ID.getMessage());
                });

        return new AdminDetails(admin);
    }
}
