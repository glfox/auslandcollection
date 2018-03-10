package com.ausland.weixin.service;

import com.ausland.weixin.model.CustomSendMessage;
import com.ausland.weixin.model.CustomSendMessageRes;

public interface WeChatMessageService {
	
	CustomSendMessageRes sendMessage(CustomSendMessage message);
}
