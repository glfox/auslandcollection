package com.ausland.weixin.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
@EnableSwagger2
@ComponentScan({ "com.ausland.weixin" })
@EnableAsync
/*@EnableJpaRepositories("com.ausland.weixin.dao")
@EnableTransactionManagement*/
public class AuslandweixinConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private Environment env;

	@Value("${multipart.file.total.size}")
	private int multipartTotalSize;

	@Value("${multipart.file.per.size}")
	private int multipartPerSize;

	@Value("${resttemplate.http.timeout}")
	private int resttemplate_http_timeout;
	
	@Value("#{'${supported.size.baby}'.split(',')}")
	public List<String> supportedBabySizeList;
	
	@Value("#{'${supported.size.children}'.split(',')}")
	public List<String> supportedChilrenSizeList;
	
	@Value("#{'${supported.size.adults}'.split(',')}")
	public List<String> supportedAdultsSizeList;
		
	@Value("#{'${supported.size.xml}'.split(',')}")
	public List<String> supportedXmlSizeList;
	
	@Value("#{'${supported.size.other}'.split(',')}")
	public List<String> supportedOtherSizeList;
	
	@Value("#{'${supported.size.category.list}'.split(',')}")
	public List<String> supportedSizeCategoryList;

	public static List<String> logisticPackageHeaders = null;
	
	public static List<String> productUploadExcelHeaders = null;
	
	public static List<String> ozlanaOrderHeaders = null;
	public static List<String> fruitOrderHeaders = null; 
	public static List<String> vitaminOrderHeaders = null;
	public static List<String> luxuryOrderHeaders = null;
	public static List<String> everOrderHeaders = null; 
	public static List<String> ozwearOrderHeaders = null; 	
	public static List<String> auSpecialOrderHeaders = null; 	
	public static List<String> mmcOrderHeaders = null;
	public static List<String> cchOrderHeaders = null;
	public static List<String> dkOrderHeaders = null;
	public static List<String> shouhouOrderHeaders = null;
	public static List<String> tasmanOrderHeaders = null;
	
	public HashMap<String, List<String>> supportedSizeCategoryMap = null;
	
	public HashMap<String, String> fromSizeToSizeCategoryMap = null;
	 
	public HashMap<String, String> getFromSizeToSizeCategoryMap()
	{
		return fromSizeToSizeCategoryMap;
	}
	
	@PostConstruct
	public void init(){
		supportedSizeCategoryMap = new HashMap<String, List<String>>();
		fromSizeToSizeCategoryMap = new HashMap<String, String>();
		for(String sizeCategory : supportedSizeCategoryList)
		{
			if(sizeCategory.equalsIgnoreCase("baby"))
			{
				supportedSizeCategoryMap.put(sizeCategory, supportedBabySizeList);
				for(String s : supportedBabySizeList)
				{
					fromSizeToSizeCategoryMap.put(s, sizeCategory);
				}
			}
			else if(sizeCategory.equalsIgnoreCase("children"))
			{
				supportedSizeCategoryMap.put(sizeCategory, supportedChilrenSizeList);
				for(String s : supportedChilrenSizeList)
				{
					fromSizeToSizeCategoryMap.put(s, sizeCategory);
				}
			}
			else if(sizeCategory.equalsIgnoreCase("adults"))
			{
				supportedSizeCategoryMap.put(sizeCategory, supportedAdultsSizeList);
				for(String s : supportedAdultsSizeList)
				{
					fromSizeToSizeCategoryMap.put(s, sizeCategory);
				}
			}
			else if(sizeCategory.equalsIgnoreCase("xml"))
			{
				supportedSizeCategoryMap.put(sizeCategory, supportedXmlSizeList);
				for(String s : supportedXmlSizeList)
				{
					fromSizeToSizeCategoryMap.put(s, sizeCategory);
				}
			}
			else if(sizeCategory.equalsIgnoreCase("other"))
			{
				supportedSizeCategoryMap.put(sizeCategory, supportedOtherSizeList);
				for(String s : supportedOtherSizeList)
				{
					fromSizeToSizeCategoryMap.put(s, sizeCategory);
				}
			}
		}
	}
	public HashMap<String, List<String>> getSupportedSizeCategoryMap()
	{
		return supportedSizeCategoryMap;
	}
	
	static {
		luxuryOrderHeaders = new ArrayList<String>();
		try{
			luxuryOrderHeaders.add("订单编号");
			luxuryOrderHeaders.add("日期");
			luxuryOrderHeaders.add("收货人地址");
			luxuryOrderHeaders.add("收货人姓名");
			luxuryOrderHeaders.add("收货人电话");
			luxuryOrderHeaders.add("买家信息");
		} catch (Exception e) {
			e.printStackTrace();
		}
		fruitOrderHeaders = new ArrayList<String>();
		try{
			fruitOrderHeaders.add("客户名称");
			fruitOrderHeaders.add("订单编号");
			fruitOrderHeaders.add("收货人姓名");
			fruitOrderHeaders.add("收货地址");
			fruitOrderHeaders.add("收货电话");
			fruitOrderHeaders.add("商品编码");
			fruitOrderHeaders.add("商品名称");
			fruitOrderHeaders.add("规格");
			fruitOrderHeaders.add("数量");
			fruitOrderHeaders.add("备注");
			fruitOrderHeaders.add("发货日期");
			fruitOrderHeaders.add("顺丰速运");
			fruitOrderHeaders.add("运单号");
		} catch (Exception e) {
			e.printStackTrace();
		}
		ozwearOrderHeaders = new ArrayList<String>();
		try{
            ozwearOrderHeaders.add("订单号");
			ozwearOrderHeaders.add("物流公司");
			ozwearOrderHeaders.add("物流单号");
			ozwearOrderHeaders.add("收件人姓名");
			ozwearOrderHeaders.add("收件人手机");
			ozwearOrderHeaders.add("收件人地址");
			ozwearOrderHeaders.add("货号");
			ozwearOrderHeaders.add("颜色");
			ozwearOrderHeaders.add("尺码");
			ozwearOrderHeaders.add("数量");
		} catch (Exception e) {
			e.printStackTrace();
		}
        auSpecialOrderHeaders = new ArrayList<String>();
		try{
            auSpecialOrderHeaders.add("订单号");
			auSpecialOrderHeaders.add("物流公司");
			auSpecialOrderHeaders.add("物流单号");
			auSpecialOrderHeaders.add("下单时间");
			auSpecialOrderHeaders.add("数量");
			auSpecialOrderHeaders.add("金额");
			auSpecialOrderHeaders.add("寄件人");
			auSpecialOrderHeaders.add("收件人");
			auSpecialOrderHeaders.add("内件");
			auSpecialOrderHeaders.add("状态");
		} catch (Exception e) {
			e.printStackTrace();
		}
		everOrderHeaders = new ArrayList<String>();
		try{
            everOrderHeaders.add("代发明细编号");
			everOrderHeaders.add("");
			everOrderHeaders.add("收件人");
			everOrderHeaders.add("收件人手机号");
			everOrderHeaders.add("");
			everOrderHeaders.add("");
			everOrderHeaders.add("");
			everOrderHeaders.add("");
			everOrderHeaders.add("");
			everOrderHeaders.add("收件人详细地址");
			everOrderHeaders.add("");
			everOrderHeaders.add("");
			everOrderHeaders.add("");
			everOrderHeaders.add("货号"); //13
			everOrderHeaders.add("颜色");
			everOrderHeaders.add("尺码");
			everOrderHeaders.add("订货数量");
			everOrderHeaders.add("发货数量");
			everOrderHeaders.add("处理状态"); //18
			everOrderHeaders.add("");
			everOrderHeaders.add("");
			everOrderHeaders.add("");
			everOrderHeaders.add("物流公司");// 22
			everOrderHeaders.add("物流单号");
		} catch (Exception e) {
			e.printStackTrace();
		}
		tasmanOrderHeaders = new ArrayList<String>();
		try{
			tasmanOrderHeaders.add("款式");
			tasmanOrderHeaders.add("发件人");
			tasmanOrderHeaders.add("名称");
			tasmanOrderHeaders.add("颜色");
			tasmanOrderHeaders.add("码数");
			tasmanOrderHeaders.add("数量");
			tasmanOrderHeaders.add("发件人电话");
			tasmanOrderHeaders.add("收件姓名");
			tasmanOrderHeaders.add("收件地址");
			tasmanOrderHeaders.add("收件电话");
			tasmanOrderHeaders.add("运单号");
			tasmanOrderHeaders.add("快递公司");
			tasmanOrderHeaders.add("日期");
		} catch (Exception e) {
			e.printStackTrace();
		}
		ozlanaOrderHeaders = new ArrayList<String>();
		try {
			ozlanaOrderHeaders.add("订单号");  // 0
			ozlanaOrderHeaders.add("");
			ozlanaOrderHeaders.add("下单时间");  // 2
			ozlanaOrderHeaders.add("");
			ozlanaOrderHeaders.add("收件人");   //4
			ozlanaOrderHeaders.add("电话号码");  // 5
			ozlanaOrderHeaders.add("");
			ozlanaOrderHeaders.add("");
			ozlanaOrderHeaders.add("产品编号");  // 12
			ozlanaOrderHeaders.add("尺码"); //13
			ozlanaOrderHeaders.add("颜色"); //14
			ozlanaOrderHeaders.add("数量");  //15
			ozlanaOrderHeaders.add("");
			ozlanaOrderHeaders.add("");
			ozlanaOrderHeaders.add("订单状态"); // 18
			ozlanaOrderHeaders.add("");
			ozlanaOrderHeaders.add("快递名称"); // 20
			ozlanaOrderHeaders.add("快递单号"); // 21
		} catch (Exception e) {
			e.printStackTrace();
		}

		vitaminOrderHeaders = new ArrayList<String>();
		try {
			vitaminOrderHeaders.add("物流单号");
			vitaminOrderHeaders.add("下单日期");
			vitaminOrderHeaders.add("下单编号");
			vitaminOrderHeaders.add("产品信息");
			vitaminOrderHeaders.add("发件人信息");
			vitaminOrderHeaders.add("收件人信息");
			vitaminOrderHeaders.add("物流公司");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		shouhouOrderHeaders = new ArrayList<String>();
		try {
			shouhouOrderHeaders.add("售后时间");
			shouhouOrderHeaders.add("收货人姓名");
			shouhouOrderHeaders.add("收货人电话");
			shouhouOrderHeaders.add("货号");
			shouhouOrderHeaders.add("售后问题");
			shouhouOrderHeaders.add("处理进度");
			shouhouOrderHeaders.add("群编号/微信号");
			shouhouOrderHeaders.add("换码费");
			shouhouOrderHeaders.add("客户寄出单号");
			shouhouOrderHeaders.add("品牌方寄出单号");
			shouhouOrderHeaders.add("是否完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dkOrderHeaders = new ArrayList<String>();
		try {
			dkOrderHeaders.add("订单编号"); // 0
			dkOrderHeaders.add("");
			dkOrderHeaders.add("");
			dkOrderHeaders.add("");
			dkOrderHeaders.add("");
			dkOrderHeaders.add("收货人"); //5
			dkOrderHeaders.add("联系电话");
			dkOrderHeaders.add("物流方式");
			dkOrderHeaders.add("物流单号");
			dkOrderHeaders.add("");
			dkOrderHeaders.add("");
			dkOrderHeaders.add("品名"); //11
			dkOrderHeaders.add("");
			dkOrderHeaders.add("规格"); //13
			dkOrderHeaders.add("");
			dkOrderHeaders.add("数量");  //15
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mmcOrderHeaders = new ArrayList<String>();
		try {
			mmcOrderHeaders.add("订单编号"); // 0
			mmcOrderHeaders.add("");
			mmcOrderHeaders.add("");
			mmcOrderHeaders.add("");
			mmcOrderHeaders.add("");
			mmcOrderHeaders.add("收货人"); //5
			mmcOrderHeaders.add("联系电话");
			mmcOrderHeaders.add("物流方式");
			mmcOrderHeaders.add("物流单号");
			mmcOrderHeaders.add("");
			mmcOrderHeaders.add("");
			mmcOrderHeaders.add("");
			mmcOrderHeaders.add("品名"); //12
			mmcOrderHeaders.add("");
			mmcOrderHeaders.add("规格"); //14
			mmcOrderHeaders.add("");
			mmcOrderHeaders.add("数量");  //16
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		cchOrderHeaders = new ArrayList<String>();
		// someothers and cch is sharing the same format
		try {
			cchOrderHeaders.add("订单编号"); // 0
			cchOrderHeaders.add("");
			cchOrderHeaders.add("");
			cchOrderHeaders.add("");
			cchOrderHeaders.add("");
			cchOrderHeaders.add("收货人"); //5
			cchOrderHeaders.add("联系电话");
			cchOrderHeaders.add("物流方式");
			cchOrderHeaders.add("物流单号");
			cchOrderHeaders.add("");
			cchOrderHeaders.add("");
			cchOrderHeaders.add("品名"); //11
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logisticPackageHeaders = new ArrayList<String>();
		try {
			logisticPackageHeaders.add("包裹重量");
			logisticPackageHeaders.add("货运单号");
			logisticPackageHeaders.add("品名");
			logisticPackageHeaders.add("收件人");
			logisticPackageHeaders.add("电话");
			logisticPackageHeaders.add("地址");
			logisticPackageHeaders.add("寄件人");
			logisticPackageHeaders.add("寄件人电话");
			logisticPackageHeaders.add("寄件人地址");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/**
		 * 产品编号	产品图片	产品品牌	产品类型	产品名称	产品颜色	产品尺码	产品毛重	自提价	包邮价
		 */
		productUploadExcelHeaders = new ArrayList<String>();
		try {
			productUploadExcelHeaders.add("产品编号");
			productUploadExcelHeaders.add("产品图片");
			productUploadExcelHeaders.add("产品品牌");
			productUploadExcelHeaders.add("产品类型");
			productUploadExcelHeaders.add("产品名称");		
			productUploadExcelHeaders.add("产品颜色");
			productUploadExcelHeaders.add("产品尺码");
			productUploadExcelHeaders.add("产品毛重");
			productUploadExcelHeaders.add("自提价");
			productUploadExcelHeaders.add("包邮价");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
 
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.ausland.weixin.controller"))
				.paths(PathSelectors.any()).build();
	}
/*
	@Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        registry.viewResolver(resolver);
    }*/
	  
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry.addResourceHandler("/index.html").addResourceLocations("/index.html");
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		//registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	    registry.addResourceHandler("webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

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

		BufferingClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(
				requestFactoryInternal);
		restTemplate.setRequestFactory(requestFactory);
		ClientHttpLoggingInterceptor clientHttpLoggingInterceptor = new ClientHttpLoggingInterceptor(requestFactory);
		restTemplate.getInterceptors().add(clientHttpLoggingInterceptor);

		return restTemplate;
	}
	/*
	 * @Bean public Jaxb2Marshaller jaxb2MarshallerSOAP() { Jaxb2Marshaller
	 * marshallerSOAP = new Jaxb2Marshaller();
	 * marshallerSOAP.setContextPaths("com.ausland.weixin.model.wsdl");
	 * marshallerSOAP.setMtomEnabled(true); return marshallerSOAP; }
	 * 
	 * @Bean WebServiceTemplate webServiceTemplate() { WebServiceTemplate
	 * template = new WebServiceTemplate();
	 * template.setMarshaller(jaxb2MarshallerSOAP());
	 * template.setUnmarshaller(jaxb2MarshallerSOAP()); return template; }
	 */

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

		config.setMaximumPoolSize(Integer.parseInt(env.getProperty("spring.datasource.hikari.maximum-pool-size")));
		config.setConnectionTimeout(Integer.parseInt(env.getProperty("spring.datasource.hikari.connection-timeout")));
		config.setMinimumIdle(Integer.parseInt(env.getProperty("spring.datasource.hikari.minimumidle")));
		config.setIdleTimeout(Long.parseLong(env.getProperty("spring.datasource.hikari.idletimeout")));
		config.setMaxLifetime(Long.parseLong(env.getProperty("spring.datasource.hikari.maxlifetime")));

		config.addDataSourceProperty("serverTimezone", "UTC");
		HikariDataSource dataSource = new HikariDataSource(config);

		return dataSource;
	}
	
	 
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedHeaders("*").allowedMethods("*").allowCredentials(true).allowedOrigins("*");
	}
}
