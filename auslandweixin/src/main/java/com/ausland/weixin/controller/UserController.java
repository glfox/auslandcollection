package com.ausland.weixin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.ausland.weixin.service.UserService;
 

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	UserService userService;
	 
	  
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
	public Boolean validateUserNameAndPassword(HttpServletRequest httpServletRequest,
		                     	HttpServletResponse httpServletResponse,
			                    @RequestParam(value="username", required = true)String userName,
			                    @RequestParam(value="password", required = true)String password)
	{
		return userService.validateUserNamePassword(userName,password);
	}
	
	public GlobalRes login(HttpServletRequest httpServletRequest,
         	HttpServletResponse httpServletResponse,
            @RequestParam(value="username", required = true)String userName,
            @RequestParam(value="password", required = true)String password)
	{
		GlobalRes res = new GlobalRes();
		if(!userService.userNameExists(userName))
		{
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			res.setErrorDetails("用户名不存在");
			return res;
		}
		
		if(userService.validateUserNamePassword(userName, password) == false)
		{
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			res.setErrorDetails("用户密码不正确");
			return res;
		}
		
	}
	
	public void logout(HttpServletRequest httpServletRequest,
         	           HttpServletResponse httpServletResponse,
         	          @CookieValue(value=AuslandApplicationConstants.COOKIE_SESSION_ID, required=true) String cookieSessionId)
	{
		
	}
}
