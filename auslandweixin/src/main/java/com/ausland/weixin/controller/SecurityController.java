package com.ausland.weixin.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ausland.weixin.util.SignUtil;
 
@Controller
public class SecurityController {
	private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);


	@RequestMapping(value = "/wxtest", method = RequestMethod.GET)
    public String test(){
        logger.debug("entered /wstest.");
        return "/index.jsp";
    }
	
	@RequestMapping(value = "/wx", method = RequestMethod.GET)
   // @RequestMapping(value = "security", method = RequestMethod.GET)
    public void WetChatGet(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam(value = "signature", required = true) String signature,
                        @RequestParam(value = "timestamp", required = true) String timestamp,
                        @RequestParam(value = "nonce", required = true) String nonce,
                        @RequestParam(value = "echostr", required = true) String echostr) throws IOException {
        try {
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                PrintWriter out = response.getWriter();
                out.print(echostr);
                out.close();
            }
        } catch (Exception e){
        	logger.debug("caught exception: "+e.getMessage());
        }
        logger.debug("连接微信公众号平台测试成功！");
    }

    @RequestMapping(value = "/wx", method = RequestMethod.POST)
    public void WetChatPost(HttpServletRequest request, HttpServletResponse response){
        //业务逻辑处理
    }
}
