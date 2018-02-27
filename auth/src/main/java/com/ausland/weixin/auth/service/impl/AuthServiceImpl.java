package com.ausland.weixin.auth.service.impl;

import javax.cache.annotation.CacheResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ausland.weixin.auth.model.AuthData;
import com.ausland.weixin.auth.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${auth.url}")
	private String auth_url;
	
	@Override
	@CacheResult(cacheName="token")
	public String getAccessToken(String appId, String appSecret, String grantType) {
		logger.info("Calling token service...");
		AuthData data = restTemplate.getForObject(auth_url, AuthData.class, grantType, appId, appSecret);
		return data.getAccess_token();
	}
}
