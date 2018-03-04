package com.ausland.weixin.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class AuslandweixinConfig {
	
	@Autowired
	private Environment env;
	
	@Value("${multipart.file.total.size}")
	private int multipartTotalSize;
	
	@Value("${multipart.file.per.size}")
	private int multipartPerSize;
	
	
	@Value("${resttemplate.http.timeout}")
	private int resttemplate_http_timeout;
	
	 
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
		CloseableHttpClient httpClient = HttpClients.custom().build(); 
		HttpComponentsClientHttpRequestFactory requestFactoryInternal = new HttpComponentsClientHttpRequestFactory();
		requestFactoryInternal.setHttpClient(httpClient);
		requestFactoryInternal.setConnectTimeout(resttemplate_http_timeout);
		requestFactoryInternal.setReadTimeout(resttemplate_http_timeout);
		
		BufferingClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(requestFactoryInternal);
		restTemplate.setRequestFactory(requestFactory);
		ClientHttpLoggingInterceptor clientHttpLoggingInterceptor = new ClientHttpLoggingInterceptor(requestFactory);
		restTemplate.getInterceptors().add(clientHttpLoggingInterceptor);
		
		return restTemplate;
	}
	/*
	@Bean
	public Jaxb2Marshaller jaxb2MarshallerSOAP() {
		Jaxb2Marshaller marshallerSOAP = new Jaxb2Marshaller();
		marshallerSOAP.setContextPaths("com.ausland.weixin.model.wsdl");
		marshallerSOAP.setMtomEnabled(true);
		return marshallerSOAP;
	}
	
	@Bean
	WebServiceTemplate webServiceTemplate() {
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMarshaller(jaxb2MarshallerSOAP());
		template.setUnmarshaller(jaxb2MarshallerSOAP());
		return template;
	}*/
	
	@Bean
	public CommonsMultipartResolver getResolver() throws IOException {
		CommonsMultipartResolver resolver = new PostPutMultipartResolver();
		resolver.setMaxUploadSize(multipartTotalSize);
		resolver.setMaxUploadSizePerFile(multipartPerSize);
		return resolver;
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
