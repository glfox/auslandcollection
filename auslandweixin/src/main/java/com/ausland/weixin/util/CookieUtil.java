package com.ausland.weixin.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.config.AuslandApplicationConstants;
 
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
