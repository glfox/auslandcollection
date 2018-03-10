package com.ausland.weixin.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.ausland.weixin.model.xml.WeChatMessage;

@Service
public interface CoreService {

	public String processRequest(WeChatMessage message,HttpServletResponse response);
	public void asyncProcessRequest(WeChatMessage message);
}
