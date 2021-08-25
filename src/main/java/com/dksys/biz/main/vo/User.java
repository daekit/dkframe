package com.dksys.biz.main.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User implements UserDetails {
	
	private static final long serialVersionUID = 7205501915991930357L;
	
	private String id;
	private String password;
	private String empNo;
	private String name;
	private String coCd;
	private String deptId;
	private String levelCd;
    private String email;
    private String enterDt;
    private String authInfo;
    private String useYn;
    private Date pwdDttm;
    private String passErrCnt;
    private String passYn;
    private String passChg;
    private String passChkCnt;
    
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}