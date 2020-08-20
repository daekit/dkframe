package com.dksys.biz.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "sap")
@Data
public class SapProperty {
	
	private String dksHost;
    private String dksSysnr;
    private String dksLang;
    private String dksClient;
    private String dksUser;
    private String dksPasswd;
    
}