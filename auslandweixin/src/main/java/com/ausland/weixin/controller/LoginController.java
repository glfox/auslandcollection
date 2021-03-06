package com.ausland.weixin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.reqres.CreateUserReq;
import com.ausland.weixin.model.reqres.GlobalRes;
import com.ausland.weixin.model.reqres.LoginStatus;
import com.ausland.weixin.model.reqres.UserForm;
import com.ausland.weixin.service.UserService;
import com.ausland.weixin.util.CookieUtil;
import com.ausland.weixin.util.UserValidator;
 
@RestController
public class LoginController {

	@Autowired
	UserService userService;
	
	@Autowired
	CookieUtil cookieUtil;
	
	@Autowired
	private UserValidator userValidator;
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

//	@RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
//    public String welcome(Model model) {
//        return "welcome";
//    }
//	
//	@RequestMapping(value = "/login", method = RequestMethod.GET)
//    public String login(Model model, String error, String logout) {
//        if (error != null)
//            model.addAttribute("error", "用户名密码有错误");
//
//        if (logout != null)
//            model.addAttribute("message", "你已经成功登出了");
//
//        return "login";
//    }
//	 
//	@RequestMapping(value = "/registration", method = RequestMethod.GET)
//    public String registration(Model model) {
//        model.addAttribute("userForm", new UserForm());
//
//        return "registration";
//    }
	 
//	@RequestMapping(value = "/registration", method = RequestMethod.POST)
//	public String registration(@ModelAttribute("userForm") UserForm userForm,
//         	                   BindingResult bindingResult, Model model)
//	{
//		userValidator.validate(userForm, bindingResult);
//
//        if (bindingResult.hasErrors()) {
//            return "registration";
//        }
//        CreateUserReq req = new CreateUserReq();
//        req.setUserName(userForm.getUsername());
//        req.setPassword(userForm.getPassword());
//        GlobalRes res = userService.createUser(req);
//        if(res == null || AuslandApplicationConstants.STATUS_FAILED.equalsIgnoreCase(res.getStatus()))
//        {
//        	bindingResult.rejectValue("username", "创建用户失败请联系客服","创建用户失败请联系客服");
//        	return "registration";
//        }
//        try {
//			userService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//        return "redirect:/welcome";
//	}

	@RequestMapping(value = "/userlogout", method = RequestMethod.POST)
	public LoginStatus logout(HttpServletRequest httpServletRequest,
         	           HttpServletResponse httpServletResponse,
         	          @CookieValue(value=AuslandApplicationConstants.COOKIE_NAME, required=false) String cookieValue)
	{
		LoginStatus res = new LoginStatus();
		cookieUtil.deleteCookie(httpServletRequest, httpServletResponse);
		res.setLogin(false);
		return res;
	}
	
	@RequestMapping(value = "/islogin", method = RequestMethod.GET)
	public LoginStatus isLogin(
         	          @CookieValue(value=AuslandApplicationConstants.COOKIE_NAME, required=false) String cookieValue) {
		
		LoginStatus res = new LoginStatus();
		if (cookieValue != null) {
			res.setLogin(true);
			res.setUserName(cookieUtil.getUserName(cookieValue));
		} else {
			res.setLogin(false);
		}
		
		return res;
	}
}
