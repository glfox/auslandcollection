package com.ausland.weixin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AuslandweixinApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuslandweixinApplication.class, args);
	}
}
