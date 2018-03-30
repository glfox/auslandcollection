package com.ausland.weixin.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.ausland.weixin.model.reqres.CreateUserReq;
import com.ausland.weixin.model.reqres.GlobalRes;
import com.ausland.weixin.model.reqres.UserRes;
 

public interface UserService {
	
	String findLoggedInUsername();
	
	UserDetails autologin(String username, String password) throws Exception;
  String validateUserNamePassword(String userName, String password);
  
  boolean userNameExists(String userName);
  
  GlobalRes createUser(CreateUserReq createUserRequest);
  
  UserRes queryUserByUserName(String userName);
  
  GlobalRes resetUserStatus(String userName,String userStatus);
  
  GlobalRes resetUserPassword(String userName, String userPassword);
 
}
