package com.dksys.biz.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "ubi")
@Data
public class UbiProperty {
	
	private String schame;
    private String host;
    private String port;
    
}