package com.ausland.weixin.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ausland.weixin.model.xml.WeChatMessage;
import com.ausland.weixin.service.CoreService;
import com.ausland.weixin.service.WeChatMessageService;
import com.ausland.weixin.util.SignUtil;

@Controller
public class SecurityController {
	private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);
    
	@Autowired
    private CoreService coreService;
	
	@Autowired
	WeChatMessageService weChatMessageService;

	@RequestMapping(value = "/wx", method = RequestMethod.GET)
	// @RequestMapping(value = "security", method = RequestMethod.GET)
	public void WetChatGet(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "signature", required = true) String signature,
			@RequestParam(value = "timestamp", required = true) String timestamp,
			@RequestParam(value = "nonce", required = true) String nonce,
			@RequestParam(value = "echostr", required = true) String echostr) throws IOException {
		try {
			// 密码
			logger.debug("entered WetChatGet.");
			if (SignUtil.checkSignature(signature, timestamp, nonce)) {
				logger.debug("验证通过");
				PrintWriter out = response.getWriter();
				out.print(echostr);
				out.close();
			}
		} catch (Exception e) {
			logger.debug("caught exception: " + e.getMessage());
		}

	}

	@RequestMapping(value = "/wx", method = RequestMethod.POST)
	public  @ResponseBody String WetChatPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("got WetChatPost.");
		ServletInputStream in = request.getInputStream();
		WeChatMessage message = JAXB.unmarshal(in, WeChatMessage.class);
		logger.debug("got message  "+message.toString());
		
		//coreService.processRequest(message, response);
		coreService.asyncProcessRequest(message);
		logger.debug("got message:"+message.getContent());
		return "success";
	}
}
