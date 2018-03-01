package com.ausland.weixin.service.impl;

import javax.cache.annotation.CacheResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ausland.weixin.model.AuthData;
import com.ausland.weixin.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${auth.url}")
	private String auth_url;
	
	@Value("${appid}")
	private String appid;
	@Value("${secret}")
	private String secret;
	@Value("${grant_type}")
	private String grant_type;

	@Override
	@CacheResult(cacheName="token")
	public String getAccessToken() {
		logger.info("Calling token service...");
		AuthData data = restTemplate.getForObject(auth_url, AuthData.class, grant_type, appid, secret);
		if(data == null) return null;
		logger.debug("got access token:"+data.toString());
		return data.getAccess_token();
	}
}
