package com.dksys.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.dksys.biz.property.SapProperty;

@SpringBootApplication
@EnableConfigurationProperties(SapProperty.class)
public class BizApplication {

	public static void main(String[] args) {
		SpringApplication.run(BizApplication.class, args);
	}

}