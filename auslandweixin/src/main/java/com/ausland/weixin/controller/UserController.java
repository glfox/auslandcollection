package com.ausland.weixin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.reqres.CreateUserReq;
import com.ausland.weixin.model.reqres.GlobalRes;
import com.ausland.weixin.model.reqres.UserForm;
import com.ausland.weixin.service.UserService;
import com.ausland.weixin.util.CookieUtil;
import com.ausland.weixin.util.UserValidator;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	CookieUtil cookieUtil;
	
	@Autowired
	private UserValidator userValidator;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	  
	/*@RequestMapping(value = "/createuser", method = RequestMethod.POST)
	public GlobalRes createUser(HttpServletRequest httpServletRequest,
		                     	HttpServletResponse httpServletResponse,
			                    @RequestBody(required = true)CreateUserReq createUserReq)
	{
		return userService.createUser(createUserReq);
	}*/
	
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

}
