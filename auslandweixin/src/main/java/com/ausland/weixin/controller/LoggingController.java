package com.ausland.weixin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ausland.weixin.service.UserService;
 

@Controller
public class LoggingController {

	  @Autowired
	  UserService userService;
	 
	  
	  
}
