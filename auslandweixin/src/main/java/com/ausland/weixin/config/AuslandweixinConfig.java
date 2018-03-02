package com.ausland.weixin.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableWebMvc
public class AuslandweixinConfig {
	
	@Autowired
	private Environment env;
	
	public static List<String> logisticPackageHeaders = null;
	
	static
	{
		logisticPackageHeaders = new ArrayList<String>();
		try
		{
			logisticPackageHeaders.add("包裹重量");
			logisticPackageHeaders.add("货运单号");
			logisticPackageHeaders.add("品名");
			logisticPackageHeaders.add("收件人");
			logisticPackageHeaders.add("电话");
			logisticPackageHeaders.add("地址");
			logisticPackageHeaders.add("寄件人");
			logisticPackageHeaders.add("寄件人电话");
			logisticPackageHeaders.add("寄件人地址");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
		converters.add(new Jaxb2RootElementHttpMessageConverter());
		converters.add(new MappingJackson2HttpMessageConverter());
		return restTemplate;
	}
	
	@Bean
	public MultipartResolver multipartResolver()
	{
		// return new StandardServletMultipartResolver();
		return new CommonsMultipartResolver();
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public DataSource dataSource() {
		
	  HikariConfig config = new HikariConfig();
	  config.setJdbcUrl(env.getProperty("spring.datasource.url"));
	  config.setUsername(env.getProperty("spring.datasource.username"));
	  config.setPassword(env.getProperty("spring.datasource.password"));
	  config.setDriverClassName(env.getProperty("spring.datasource.driverclass"));
	  config.setPoolName(env.getProperty("spring.datasource.hikari.poolname"));

	  config.addDataSourceProperty("maximumPoolSize", Integer.parseInt(env.getProperty("spring.datasource.hikari.maximum-pool-size")));
	  config.addDataSourceProperty("connectionTimeout", Integer.parseInt(env.getProperty("spring.datasource.hikari.connection-timeout")));
	  
	  HikariDataSource dataSource = new HikariDataSource(config);

		return dataSource;
	}

	@Bean
	public PlatformTransactionManager txManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}
