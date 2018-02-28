package com.ausland.weixin.service;

import com.ausland.weixin.model.CustomSendMessage;

public interface WeChatMessageService {
	
	void sendMessage(CustomSendMessage message);
}
