package com.ausland.weixin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.reqres.CreateUserReq;
import com.ausland.weixin.model.reqres.GlobalRes;
import com.ausland.weixin.model.reqres.UserRes;
import com.ausland.weixin.service.UserService;
import com.ausland.weixin.util.CookieUtil;
import com.ausland.weixin.util.CustomCookie;
import com.ausland.weixin.util.DataEncryptionDecryptionUtil;
 

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	CookieUtil cookieUtil;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	  
	@RequestMapping(value = "/createuser", method = RequestMethod.POST)
	public GlobalRes createUser(HttpServletRequest httpServletRequest,
		                     	HttpServletResponse httpServletResponse,
			                    @RequestBody(required = true)CreateUserReq createUserReq)
	{
		return userService.createUser(createUserReq);
	}
	
	@RequestMapping(value = "/checkusernameexists", method = RequestMethod.GET)
	public Boolean userNameExists(HttpServletRequest httpServletRequest,
		                     	HttpServletResponse httpServletResponse,
			                    @RequestParam(value="username", required = true)String userName)
	{
		return userService.userNameExists(userName);
	}
	
	@RequestMapping(value = "/validateuser", method = RequestMethod.POST)
	public GlobalRes validateUserNameAndPassword(HttpServletRequest httpServletRequest,
		                     	HttpServletResponse httpServletResponse,
			                    @RequestParam(value="username", required = true)String userName,
			                    @RequestParam(value="password", required = true)String password)
	{
		GlobalRes res = new GlobalRes();
		String ret = userService.validateUserNamePassword(userName,password);
		if(StringUtils.isEmpty(ret))
		{
			res.setStatus(AuslandApplicationConstants.STATUS_OK);
		}
		else
		{
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			res.setErrorDetails(ret);
		}
		return res;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public GlobalRes login(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
         	               @CookieValue(value=AuslandApplicationConstants.COOKIE_NAME, required=false) String cookieValue,
                           @RequestParam(value="username", required = false)String userName,
                           @RequestParam(value="password", required = false)String password)
	{
		GlobalRes res = new GlobalRes();
		if(StringUtils.isEmpty(cookieValue))
		{
			if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password))
			{
				res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				res.setErrorDetails("没有输入用户名密码");
				return res;
			}
			if(!userService.userNameExists(userName))
			{
				res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				res.setErrorDetails("用户名不存在");
				return res;
			}
			String ret = userService.validateUserNamePassword(userName, password);
			if(StringUtils.isEmpty(ret))
			{
				res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				res.setErrorDetails(ret);
				return res;
			}
			
			UserRes userRes = userService.queryUserByUserName(userName);
			if(userRes == null || userRes.getUserName() == null)
			{
				res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				res.setErrorDetails("数据库里没有找到该用户："+userName);
				return res;
			}
			CustomCookie cc = new CustomCookie();
			cc.setRole(userRes.getRole());
			cc.setUserId(userRes.getUserId());
			cc.setUserName(userRes.getUserName());
			cookieUtil.addOrUpdateCookie(cc, httpServletRequest, AuslandApplicationConstants.COOKIE_EXPIRATION_INSECONDS, httpServletResponse);	
			res.setStatus(AuslandApplicationConstants.STATUS_OK);
			return res;
		}
		else
		{
			try {
				CustomCookie cc = DataEncryptionDecryptionUtil.getCustomCookieObjectFromCookieValue(cookieValue);
				if(cc != null && cc.getUserName() != null)
				{
					res.setStatus(AuslandApplicationConstants.STATUS_OK);
					return res;
				}
			} catch (Exception e) {
			    logger.debug("got exception:"+e.toString());	
			}
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			res.setErrorDetails("无法解析cookie，请重新登陆。");
			return res;
		}
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public void logout(HttpServletRequest httpServletRequest,
         	           HttpServletResponse httpServletResponse/*,
         	          @CookieValue(value=AuslandApplicationConstants.COOKIE_NAME, required=false) String cookieValue*/)
	{
		cookieUtil.deleteCookie(httpServletRequest, httpServletResponse);
	}
}
