package com.ausland.weixin.service;

import com.ausland.weixin.model.WeChatMessage;

public interface WeChatMessageService {
	
	void sendMessage(WeChatMessage message);
}
