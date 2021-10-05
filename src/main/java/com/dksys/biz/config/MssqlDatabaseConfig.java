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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

/*
	@Configuration: Bean을 컨테이너에 등록하는 스프링 설정 클래스임을 선언한다.
 */
@Configuration
/* 
	@EnableTransactionManagement: 명시적인트랜잭션(@Transactional) 처리를 활성화한다.
*/
@EnableTransactionManagement
/* 
	@MapperScan: @Mapper어노테이션을 선언하여 빈에 등록되어있는 인터페이스를 스캔한다.
	basePackages: 인테페이스 스캔을 수행할 패키지를 지정한다.
	sqlSessionFactoryRef
	 - 이 속성을 사용하지 않는다면 두 개 이상의 @MapperScan을 사용할 때 에러가 발생한다.
	 - 스캔한 인터페이스가 사용할 SqlSessionFactory를 지정한다.
*/
@MapperScan(basePackages = "com.dksys.biz.**.mssqlmapper", sqlSessionFactoryRef = "mssqlSqlSessionFactory")
public class MssqlDatabaseConfig {
	
/*
	@Bean(name="mssqlDataSource"): mssqlDataSource 이름으로 컨테이너에 DataSource타입의 Bean을 등록한다.
	@ConfigurationProperties: yml에서 접두사를 spring.datasource.mssql로 가지는 데이터를 읽어온다.
*/
    @Bean(name="mssqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mssql")
    public DataSource mssqlDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
    
    @Bean(name="mssqlSqlSessionFactory")
    public SqlSessionFactory mssqlSqlSessionFactory(@Qualifier("mssqlDataSource") DataSource mssqlDataSource, ApplicationContext applicationContext) throws Exception {
       SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
       sqlSessionFactoryBean.setDataSource(mssqlDataSource);
       sqlSessionFactoryBean.setTypeAliasesPackage("com.dksys.biz");
       sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/mssql/**/*.xml"));
       return sqlSessionFactoryBean.getObject();
    }

    @Bean(name="mssqlSqlSessionTemplate")
    public SqlSessionTemplate mssqlSqlSessionTemplate(@Qualifier("mssqlSqlSessionFactory") SqlSessionFactory mssqlSqlSessionFactory) {
        return new SqlSessionTemplate(mssqlSqlSessionFactory);
    }
    
    @Bean(name="mssqlTransactionManager")
    public DataSourceTransactionManager mssqlTransactionManager() {
    	DataSourceTransactionManager mssqlTransactionManager = new DataSourceTransactionManager();
    	mssqlTransactionManager.setDataSource(mssqlDataSource());
        return mssqlTransactionManager;
    }
	
}