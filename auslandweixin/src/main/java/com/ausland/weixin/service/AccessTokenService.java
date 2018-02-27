package com.ausland.weixin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccessTokenService {

	@Autowired
	RestTemplate restTemplate;
	
	String getAccessToken()
	{
		return "";
	}
}
