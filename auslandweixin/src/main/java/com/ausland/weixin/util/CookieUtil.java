package com.ausland.weixin.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
 
@Service
public class CookieUtil {
 
	private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);
 
	public static void main(String[] args) {
		 
	}
	
	public void addOrUpdateCookie(Cookie customCookie, HttpServletRequest request, int expiry, HttpServletResponse resp, String callerName)
	{
		Cookie cookie = new Cookie(callerName, callerName);
	}
	
	 
}
