package com.ausland.weixin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ausland.weixin.service.AccessTokenService;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

	@Value("${appid}")
	private String appId;
	
	@Value("${secret}")
	private String secret;
	
	@Value("${token.proxy}")
	private String url;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public String getAccessToken() {
		// TODO Auto-generated method stub
		return restTemplate.getForObject(url, String.class, appId, secret);
	}
}
