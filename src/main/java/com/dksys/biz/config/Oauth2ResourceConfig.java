package com.dksys.biz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class Oauth2ResourceConfig extends ResourceServerConfigurerAdapter {
//	private String secretKey = "biz2020";

	@Override
    public void configure(ResourceServerSecurityConfigurer resources) {
      
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
      			.antMatchers("/", "/login", "/static/**", "/oauth/token", "/oauth/**", "/download/**", "/ws/**", "/admin/cm/cm08/**").permitAll()
      			.anyRequest().authenticated();
    }
	
}