package com.ausland.weixin.service.impl;

import java.util.Date;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ausland.weixin.model.CustomSendMessage;
import com.ausland.weixin.model.xml.WeChatMessage;
import com.ausland.weixin.service.CoreService;
import com.ausland.weixin.service.WeChatMessageService;

@Service
public class CoreServiceImpl implements CoreService {

	private static final Logger logger = LoggerFactory.getLogger(CoreServiceImpl.class);
	
	@Value("${message.send.url}")
	private String messageSendUrl;

	@Autowired
	private WeChatMessageService weChatMessageService;

	@Override
	public String processRequest(WeChatMessage message) {
		String serverName = message.getToUserName();
		String userName = message.getFromUserName();
		CustomSendMessage newMsg = new CustomSendMessage();
		newMsg.setFromUserName(serverName);
		newMsg.setToUserName(userName);
		newMsg.setMsgType("text");
		newMsg.setCreateTime(new Date().getTime());
		newMsg.setContent("Hi There!");
		
		weChatMessageService.sendMessage(newMsg);
		return "success";
	}
}
