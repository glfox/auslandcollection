package com.ausland.weixin.service;

import com.ausland.weixin.model.CustomSendMessage;
import com.ausland.weixin.model.xml.WeChatMessage;

public interface WeChatMessageService {
	
	void sendMessage(String message);
}
