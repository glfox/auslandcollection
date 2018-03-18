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
 
@Controller
public class LoginController {

	@Autowired
	UserService userService;
	
	@Autowired
	CookieUtil cookieUtil;
	
	@Autowired
	private UserValidator userValidator;
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {
        return "welcome";
    }
	
	 @RequestMapping(value = "/login", method = RequestMethod.GET)
	    public String login(Model model, String error, String logout) {
	        if (error != null)
	            model.addAttribute("error", "用户名密码有错误");

	        if (logout != null)
	            model.addAttribute("message", "你已经成功登出了");

	        return "login";
	    }
	 
	 @RequestMapping(value = "/registration", method = RequestMethod.GET)
	    public String registration(Model model) {
	        model.addAttribute("userForm", new UserForm());

	        return "registration";
	    }
	 
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String registration(@ModelAttribute("userForm") UserForm userForm,
         	                   BindingResult bindingResult, Model model)
	{
		userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        CreateUserReq req = new CreateUserReq();
        req.setUserName(userForm.getUsername());
        req.setPassword(userForm.getPassword());
        userService.createUser(req);

        userService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public void logout(HttpServletRequest httpServletRequest,
         	           HttpServletResponse httpServletResponse/*,
         	          @CookieValue(value=AuslandApplicationConstants.COOKIE_NAME, required=false) String cookieValue*/)
	{
		cookieUtil.deleteCookie(httpServletRequest, httpServletResponse);
	}
}
