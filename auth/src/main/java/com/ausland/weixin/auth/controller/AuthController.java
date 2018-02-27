package com.ausland.weixin.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ausland.weixin.auth.service.AuthService;

@RestController
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(value = "/token", method = RequestMethod.GET)
	public String getAccessToken(@RequestParam(value = "appid") String appId, 
			@RequestParam(value = "secret") String appSecret, 
			@RequestParam(value = "grant_type") String grantType) {
		
		return authService.getAccessToken(appId, appSecret, grantType);
	}
}
