package com.ausland.weixin.util;

import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ausland.weixin.config.AuslandApplicationConstants;
 
@Service
public class CookieUtil {
 
	private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);
 
	@Value("${test.mode.enabled}")
	private Boolean testModeEnabled;
	
	@Value("${test.user}")
	private String testUser;
	
	public static void main(String[] args) {
		 
	}
	
	
	public CustomCookie getCustomCookieObjectFromCookieValue(String cookieValue) throws Exception
	{
		byte[] bytes = Base64.getDecoder().decode(cookieValue);
		String decryptedString =  new String(bytes, "utf-8");
				//decryption(cookieValue,AuslandApplicationConstants.COOKIE_ENCRYPTION_KEY);
		if(StringUtils.isEmpty(decryptedString))
			return null;
		String[] inputs = decryptedString.split(",");
		if(inputs.length < 3)
			return null;
		CustomCookie cc = new CustomCookie();
		cc.setPassword(inputs[0]);
		//cc.setUserId(Integer.parseInt(inputs[0]));
		cc.setUserName(inputs[1]);
		cc.setRole(inputs[2]);
		return cc;
	}
	
	public String encryptCookieValueFromCookieObject(CustomCookie customCookie)  throws Exception
	{
		if(customCookie == null) return null;
		return Base64.getEncoder().encodeToString(customCookie.toString().getBytes("utf-8"));
		
		//return encryption(customCookie.toString(), AuslandApplicationConstants.COOKIE_ENCRYPTION_KEY, customCookie.getUserName()); 
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
	
	public CustomCookie validateCookieValue(String cookieValue) throws Exception
	{
		if(StringUtils.isEmpty(cookieValue))
		{
			if(testModeEnabled && !StringUtils.isEmpty(testUser))
			{
				CustomCookie cc = new CustomCookie();
				cc.setRole(AuslandApplicationConstants.ADMIN_USER_ROLE);
				cc.setUserName(testUser);
				cc.setPassword("12345678");
				return cc;
			}
			return null;
		}
		return getCustomCookieObjectFromCookieValue(cookieValue);
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
			encryptedString = encryptCookieValueFromCookieObject(customCookie);
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
	 
}
