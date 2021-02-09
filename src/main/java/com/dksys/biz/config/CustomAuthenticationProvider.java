package com.dksys.biz.config;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dksys.biz.main.service.LoginService;
import com.dksys.biz.main.vo.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    LoginService loginService;
    
    @Override
    public Authentication authenticate(Authentication authentication) {

        @SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) authentication.getDetails();
        String password = authentication.getCredentials().toString();

        User user = loginService.selectUserInfo(map);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new BadCredentialsException("password is not valid");

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
