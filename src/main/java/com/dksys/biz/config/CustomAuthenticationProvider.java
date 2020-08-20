package com.dksys.biz.config;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dksys.biz.user.UserRepository;
import com.dksys.biz.user.vo.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) {

        @SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) authentication.getDetails();
        String name = map.get("id");
        String password = authentication.getCredentials().toString();

        User user = userRepository.findById(name).orElseThrow(() -> new UsernameNotFoundException("user is not exists"));

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new BadCredentialsException("password is not valid");

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
