package com.dksys.biz.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.dksys.biz.**.mapper", sqlSessionFactoryRef = "erpSqlSessionFactory")
public class DatabaseConfig {
	
//	/** DataSource Main 생성 */
	@Primary
    @Bean(name = "erpDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource erpDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }
    
    @Primary
    @Bean(name = "erpSqlSessionFactory")
    public SqlSessionFactory erpSqlSessionFactory(@Qualifier("erpDataSource") DataSource erpDataSource,
                                ApplicationContext applicationContext) throws Exception {
           SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
           sqlSessionFactoryBean.setDataSource(erpDataSource);
           sqlSessionFactoryBean.setTypeAliasesPackage("com.dksys.biz");
           sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/**/*.xml"));
           return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean(name = "erpSessionTemplate")
    public SqlSessionTemplate erpSqlSessionTemplate(@Qualifier("erpSqlSessionFactory") SqlSessionFactory erpSqlSessionFactory) {
        return new SqlSessionTemplate(erpSqlSessionFactory);
    }
    
// 
//    /** DataSource Sub 생성 */
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource2")
//    public DataSource mesDataSource() {
//        return DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .build();
//    }
	
//	@Bean
//	public SqlSessionFactory sqlSessionFactory(DataSource datasource) throws Exception {
//		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
//		sqlSessionFactory.setDataSource(datasource);
//		sqlSessionFactory.setTypeAliasesPackage("com.dksys.biz");
//		sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/**/*.xml"));
//		return sqlSessionFactory.getObject();
//	}
//	
//	@Bean
//	public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
//		return new SqlSessionTemplate(sqlSessionFactory);
//	}
	
}