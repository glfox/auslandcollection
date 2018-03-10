package com.ausland.weixin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ausland.weixin.model.CustomSendMessage;
import com.ausland.weixin.model.CustomSendMessageRes;
import com.ausland.weixin.service.AuthService;
import com.ausland.weixin.service.WeChatMessageService;

@Service
public class WeChatMessageServiceImpl implements WeChatMessageService {

	private static final Logger logger = LoggerFactory.getLogger(WeChatMessageServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private AuthService accessTokenService;
	
	@Value("${message.send.url}")
	private String msgSendUrl;
	
	@Override
	public CustomSendMessageRes sendMessage(CustomSendMessage message) {
		logger.info("Send message:" + message);
		String accessToken = accessTokenService.getAccessToken();
		CustomSendMessageRes res = restTemplate.postForObject(msgSendUrl, message, CustomSendMessageRes.class, accessToken);
		logger.info("Send message res. " + res);
		return res;
	}

	private HttpHeaders buildHttpHeader(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken));
		return headers;
	}
}
