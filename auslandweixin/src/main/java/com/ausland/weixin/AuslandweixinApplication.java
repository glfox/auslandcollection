package com.ausland.weixin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
 

@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
public class AuslandweixinApplication extends SpringBootServletInitializer{
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AuslandweixinApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(AuslandweixinApplication.class, args);
	}
}
