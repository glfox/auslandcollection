package com.ausland.weixin.auth.service;

public interface AuthService {
	
	String getAccessToken(String appId, String appSecret, String grantType);
}
