package com.ausland.weixin.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ausland.weixin.config.AuslandApplicationConstants;
 
@Service
public class CookieUtil {
 
	private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);
 
	public static void main(String[] args) {
		 
	}
	
	public void deleteCookie(HttpServletRequest request, HttpServletResponse resp)
	{
		if(request == null || resp == null)
			return;
		Cookie[] c_array = request.getCookies();
		for(Cookie cookie : c_array)
		{
			if(AuslandApplicationConstants.COOKIE_NAME.equalsIgnoreCase(cookie.getName()))
			{
				cookie.setMaxAge(0);
				cookie.setSecure(false);
				cookie.setPath(request.getContextPath()+"/");
				resp.addCookie(cookie);
				return;
			}
		}
		logger.debug("did not find the cookie, return.");
	}
	
	public void addOrUpdateCookie(CustomCookie customCookie, HttpServletRequest request, int expiry, HttpServletResponse resp)
	{
		if(request == null || resp == null || customCookie == null)
			return;
		Cookie[] c_array = request.getCookies();
		Cookie aulandCookie = null;
		for(Cookie cookie : c_array)
		{
			if(AuslandApplicationConstants.COOKIE_NAME.equalsIgnoreCase(cookie.getName()))
			{
				aulandCookie = cookie;
				break;
			}
		}
		
		String encryptedString = null;
		try {
			encryptedString = DataEncryptionDecryptionUtil.encryptCookieValueFromCookieObject(customCookie);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("caught exception during encrypt cookie:"+e.getMessage());
			return;
		}
		if(StringUtils.isEmpty(encryptedString))
			return;
		if(aulandCookie == null)
			aulandCookie = new Cookie(AuslandApplicationConstants.COOKIE_NAME, encryptedString);
		else
			aulandCookie.setValue(encryptedString);
		aulandCookie.setMaxAge(expiry);
		aulandCookie.setPath(request.getContextPath()+"/");
		aulandCookie.setSecure(false);
		resp.addCookie(aulandCookie);
		return;
	}
	 
	public String getUserName(String cookieValue) {
		return DataEncryptionDecryptionUtil.getCustomCookieObjectFromCookieValue(cookieValue).getUserName();
	}
}
