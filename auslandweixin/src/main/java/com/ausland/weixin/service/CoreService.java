package com.ausland.weixin.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

@Service
public class CoreService {

	public  String processRequest(HttpServletRequest request) {
	    //暂时对消息不作处理
	      return "";
	    }
	
}
