package com.obigo.microev.tms.admin.config.security;


import com.obigo.microev.tms.core.domain.entity.Admin;
import com.obigo.microev.tms.core.domain.enumeration.AdminStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "회원 Principal 사용 용도")
public class AdminDetails implements UserDetails {
    private Long adminSeq;
    private String email;
    private String password;
    private String name;
    private String contact;
    private String position;
    private AdminStatus status;
    private Collection<SimpleGrantedAuthority> authorities;


    public AdminDetails(Admin admin) {
        this.adminSeq = admin.getAdminSeq();
        this.email = admin.getEmail();
        this.password = admin.getPassword();
        this.name = admin.getAdminName();
        this.contact = admin.getContact();
        this.position = admin.getPosition();
        this.status = AdminStatus.valueOf(admin.getStatusCd());
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return AdminStatus.Locked != status;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return AdminStatus.Normal == status;
    }

    public boolean isDormant() {
        return AdminStatus.Dormancy.equals(status);
    }
}
