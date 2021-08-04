package com.dksys.biz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.dksys.biz.cmn.vo.Klnet;

@Configuration
@ConfigurationProperties(prefix = "spring")
public class YamlReader {
	private Klnet klnet;

	public Klnet getKlnet() {
		return klnet;
	}

	public void setKlnet(Klnet klnet) {
		this.klnet = klnet;
	}
	
}
