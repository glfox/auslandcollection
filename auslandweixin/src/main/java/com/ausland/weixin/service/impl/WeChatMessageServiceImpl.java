package com.ausland.weixin.service.impl;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.ausland.weixin.model.WeChatMessage;
import com.ausland.weixin.service.AccessTokenService;
import com.ausland.weixin.service.WeChatMessageService;

public class WeChatMessageServiceImpl implements WeChatMessageService {

	private static final Logger logger = LoggerFactory.getLogger(WeChatMessageServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private AccessTokenService accessTokenService;
	
	@Value("${message.send.url}")
	private String msgSendUrl;
	
	@Override
	public void sendMessage(WeChatMessage message) {
		logger.info("Send message to " + message.getToUserName() + " from " + message.getFromUserName() + ", message: " + message.getContent());
		String accessToken = accessTokenService.getAccessToken();
		HttpHeaders requestHeader = buildHttpHeader(accessToken);
		HttpEntity<WeChatMessage> requestEntity = new HttpEntity<>(message, requestHeader);
		ResponseEntity<Object> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(new URI(msgSendUrl), HttpMethod.POST, requestEntity, Object.class);
		} catch (Exception e) {
			logger.error(" Failed to send message. " + e.getMessage());
		}
		logger.info("Message sent successfully. " + responseEntity.getStatusCode().toString());
	}

	private HttpHeaders buildHttpHeader(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken));
		return headers;
	}
}
