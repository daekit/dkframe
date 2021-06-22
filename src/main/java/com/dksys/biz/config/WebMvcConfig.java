package com.dksys.biz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.dksys.biz.util.MessageUtils;
import com.dksys.biz.util.WebClientUtil;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Bean
	public MappingJackson2JsonView jsonView() {
		return new MappingJackson2JsonView();
    }
	
	@Bean
	public MessageUtils messageUtils() {
		return new MessageUtils();
	}
	
	@Bean
	public WebClientUtil webClientUtil() {
		return new WebClientUtil();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Override
	// device 인터셉터 추가
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new DeviceResolverHandlerInterceptor());
	}
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .maxAge(3000);
	}
 	
}