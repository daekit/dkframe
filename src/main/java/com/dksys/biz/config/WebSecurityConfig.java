package com.dksys.biz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private CustomAuthenticationProvider authenticationProvider;
	
	@Override @Bean public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean(); 
	}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }
    
      

	@Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .csrf().disable()
                .headers().frameOptions().disable().and()
                .authorizeRequests().antMatchers("/oauth/**", "/oauth/token", "/oauth2/callback", "/h2-console/*").permitAll().and()                
                .formLogin().and()                
                .cors().and()                
                
              // .logout().logoutUrl("/logout").logoutSuccessUrl("/login")
               // .invalidateHttpSession(true)
              //  .deleteCookies("jwtToken").and()
                .httpBasic().disable();
        
    }
} 
