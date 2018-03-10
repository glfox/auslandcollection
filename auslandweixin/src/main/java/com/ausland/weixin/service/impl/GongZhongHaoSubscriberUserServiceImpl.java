package com.ausland.weixin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ausland.weixin.model.reqres.GongZhongHaoUserInfoRes;
import com.ausland.weixin.service.AuthService;
import com.ausland.weixin.service.GongZhongHaoSubscriberUserService;

@Service
public class GongZhongHaoSubscriberUserServiceImpl implements GongZhongHaoSubscriberUserService{

private static final Logger logger = LoggerFactory.getLogger(GongZhongHaoSubscriberUserServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private AuthService accessTokenService;
	
	@Value("${wechat.userinfo.url}")
	private String msgSendUrl;
	
	@Override
	public GongZhongHaoUserInfoRes getWeChatUserInfo(String openId) {
		logger.info("getWeChatUserInfo entered with openid:" + openId);
		String accessToken = accessTokenService.getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<GongZhongHaoUserInfoRes> res = restTemplate.exchange(msgSendUrl, HttpMethod.GET, entity, GongZhongHaoUserInfoRes.class,accessToken, openId);
		if(res == null || HttpStatus.OK != res.getStatusCode())
		{
			return null;
		}
		logger.debug("res.getBody():"+res.getBody());
		return res.getBody();
	}
	
	

}
