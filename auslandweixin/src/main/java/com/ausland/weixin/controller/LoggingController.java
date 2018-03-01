package com.ausland.weixin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ausland.weixin.model.db.User;
import com.ausland.weixin.service.UserService;
 

@Controller
public class LoggingController {

	  @Autowired
	  UserService userService;
	 
	  
	  
}
