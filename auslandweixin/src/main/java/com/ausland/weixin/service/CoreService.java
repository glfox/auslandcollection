package com.ausland.weixin.service;

import org.springframework.stereotype.Service;

import com.ausland.weixin.model.xml.WeChatMessage;

@Service
public interface CoreService {

	public String processRequest(WeChatMessage message);
}
