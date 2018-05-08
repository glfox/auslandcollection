package com.ausland.weixin.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.reqres.CreateUserReq;
import com.ausland.weixin.model.reqres.GlobalRes;
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
	
	/*@Autowired
	private UserValidator userValidator;
	*/
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	  
	@RequestMapping(value = "/createuser", method = RequestMethod.POST)
	public GlobalRes createUser(HttpServletRequest httpServletRequest,
		                     	HttpServletResponse httpServletResponse,
			                    @RequestBody(required = true)CreateUserReq createUserReq)
	{
		GlobalRes res = new GlobalRes();
		try
		{
			GlobalRes cres = userService.createUser(createUserReq);
			if(cres == null || AuslandApplicationConstants.STATUS_FAILED.equalsIgnoreCase(cres.getStatus()))
			{
				return cres;
			}
			UserDetails userDetails = userService.autologin(createUserReq.getUserName(), createUserReq.getPassword());
			if(userDetails != null)
			{
				CustomCookie cookie = new CustomCookie();
				String role = null;
				if(userDetails.getAuthorities() != null)
				{
					Set<GrantedAuthority> set = (Set<GrantedAuthority>) userDetails.getAuthorities();
					for(GrantedAuthority ga : set)
					{
						role = ga.getAuthority();
						break;
					}
				}
				cookie.setRole(role);
				cookie.setPassword(userDetails.getPassword());
				cookie.setUserName(createUserReq.getUserName());
				cookieUtil.addOrUpdateCookie(cookie, httpServletRequest, AuslandApplicationConstants.COOKIE_EXPIRATION_INSECONDS, httpServletResponse);
				res.setStatus(AuslandApplicationConstants.STATUS_OK);
				return res;
			}
			res.setErrorDetails("创建过程中验证用户失败");
		}
		catch(Exception e)
		{
			res.setErrorDetails("创建过程中验证用户失败："+e.getMessage());
		}
		res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
		return res;
	}
	
	/*@RequestMapping(value = "/checkusernameexists", method = RequestMethod.GET)
	public Boolean userNameExists(HttpServletRequest httpServletRequest,
		                     	HttpServletResponse httpServletResponse,
			                    @RequestParam(value="username", required = true)String userName)
	{
		return userService.userNameExists(userName);
	}
	*/
	@RequestMapping(value = "/validateuser", method = RequestMethod.POST)
	public GlobalRes validateUserNameAndPassword(HttpServletRequest httpServletRequest,
		                     	HttpServletResponse httpServletResponse,
			                    @RequestParam(value="username", required = true)String userName,
			                    @RequestParam(value="password", required = true)String password,
			                    @CookieValue(value=AuslandApplicationConstants.COOKIE_NAME, required=false) String cookieValue)
	{
		GlobalRes res = new GlobalRes();
		if(cookieValue != null)
		{
			logger.debug("got the cookie:"+cookieValue);
			try {
				CustomCookie cc = DataEncryptionDecryptionUtil.getCustomCookieObjectFromCookieValue(cookieValue);
				logger.debug("parsed cookie to the string:"+cc.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			logger.debug("no cookie detected.");
		}
		String ret = userService.validateUserNamePassword(userName,password);
		if(StringUtils.isEmpty(ret))
		{
			try
			{
				UserDetails userDetails = userService.autologin(userName, password);
				if(userDetails != null)
				{
					CustomCookie cookie = new CustomCookie();
					String role = null;
					if(userDetails != null || userDetails.getAuthorities() != null)
					{
						Set<GrantedAuthority> set = (Set<GrantedAuthority>) userDetails.getAuthorities();
						for(GrantedAuthority ga : set)
						{
							role = ga.getAuthority();
							break;
						}
					}
					cookie.setRole(role);
					cookie.setUserName(userName);
					cookie.setPassword(userDetails.getPassword());
					cookieUtil.addOrUpdateCookie(cookie, httpServletRequest, AuslandApplicationConstants.COOKIE_EXPIRATION_INSECONDS, httpServletResponse);
					res.setStatus(AuslandApplicationConstants.STATUS_OK);
					return res;
				}
				res.setErrorDetails("验证用户失败");
			}
			catch(Exception e)
			{
				res.setErrorDetails("验证用户失败："+e.getMessage());
			}
		}
		else
		{
			res.setErrorDetails(ret);
		}
		res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
		return res;
	}
	
	@RequestMapping(value = "/resetuserstatus", method = RequestMethod.POST)
	public GlobalRes resetUserStatus(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse, 
                           @RequestParam(value="username", required = true)String userName,
                           @RequestParam(value="newstatus", required = true)String userStatus)
	{
		return userService.resetUserStatus(userName, userStatus);
	}
	
	@RequestMapping(value = "/resetuserpassword", method = RequestMethod.POST)
	public GlobalRes resetUserPassword(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse, 
                           @RequestParam(value="username", required = true)String userName,
                           @RequestParam(value="password", required = true)String password)
	{
		return userService.resetUserPassword(userName, password);
	}
	
//	@RequestMapping(value = "/logout", method = RequestMethod.POST)
//	public void logout(HttpServletRequest httpServletRequest,
//         	           HttpServletResponse httpServletResponse)
//	{
//		cookieUtil.deleteCookie(httpServletRequest, httpServletResponse);
//	}

}
