package com.ausland.weixin.service.impl;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.CustomSendMessage;
import com.ausland.weixin.model.CustomSendMessageRes;
import com.ausland.weixin.model.MessageContent;
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
		if(message != null 
		&& AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT.equalsIgnoreCase(message.getMsgType()) 
		&& message.getContent() != null)
		{
	        
			String str = message.getContent().getContent();
			try
			{
				byte[] bytes = str.getBytes("utf-8");
		        if(bytes.length > AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT_MAXLENGTH)
		        {
		        	int startIndex = 0;
		        	int length = AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT_MAXLENGTH;
		        	while(startIndex < bytes.length)
		        	{
		        		byte[] bytenew = new byte[length];
		        		System.arraycopy(bytes, startIndex, bytenew, 0, length);
			        	String content = new String(bytenew);
			        	
			        	MessageContent mc = new MessageContent();
			        	mc.setContent(content);
			        	CustomSendMessage mesg = new CustomSendMessage();
			        	mesg.setContent(mc);
			        	mesg.setToUserName(message.getToUserName());
			        	mesg.setMsgType(message.getMsgType());
			        	CustomSendMessageRes res = restTemplate.postForObject(msgSendUrl, mesg, CustomSendMessageRes.class, accessToken);
			    		logger.info("Send message got res. " + res.toString());
			    		startIndex = startIndex + length;
			    		length = Math.min(AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT_MAXLENGTH, bytes.length - startIndex);
		        	}
		        	CustomSendMessageRes res = new CustomSendMessageRes();
		        	res.setErrorcode(AuslandApplicationConstants.STATUS_OK);
		        	return res;
		        }
			}
			catch(Exception e)
			{
				logger.debug("got exception:"+e.getMessage());
				MessageContent mc = new MessageContent();
	        	mc.setContent("数据解析异常："+e.getMessage());
				message.setContent(mc);
			}
		}
		CustomSendMessageRes res = restTemplate.postForObject(msgSendUrl, message, CustomSendMessageRes.class, accessToken);
		logger.info("Send message got res. " + res.toString());
		return res;
	}

	private HttpHeaders buildHttpHeader(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken));
		return headers;
	}
}
