package com.ausland.weixin.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ausland.weixin.model.WeChatMessage;
import com.ausland.weixin.service.AccessTokenService;
import com.ausland.weixin.service.CoreService;

@Service
public class CoreServiceImpl implements CoreService {

	@Autowired
	private AccessTokenService accessTokenService;
	
	@Value("${message.send.url}")
	private String messageSendUrl;
	
	@Override
	public String processRequest(WeChatMessage message) {
		String accessToken = accessTokenService.getAccessToken();
		String serverName = message.getToUserName();
		String userName = message.getFromUserName();
		WeChatMessage newMsg = new WeChatMessage();
		newMsg.setFromUserName(serverName);
		newMsg.setToUserName(userName);
		newMsg.setMsgType("text");
		newMsg.setCreateTime(new Date().getTime());
		newMsg.setContent("Hi There!");
		
		
		return null;
	}
}
