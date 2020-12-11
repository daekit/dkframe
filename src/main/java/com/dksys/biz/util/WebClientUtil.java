package com.dksys.biz.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientUtil {

	@Autowired
    WebClient.Builder builder;
    WebClient webClient;

    public String get(String url) {
    	webClient = builder.build();
    	return webClient.get() 
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
	}
    
}
