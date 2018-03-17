package com.ausland.weixin.service;

import com.ausland.weixin.model.reqres.CreateUserReq;
import com.ausland.weixin.model.reqres.GlobalRes;
import com.ausland.weixin.model.reqres.UserRes;
 

public interface UserService {
	
  String validateUserNamePassword(String userName, String password);
  
  boolean userNameExists(String userName);
  
  GlobalRes createUser(CreateUserReq createUserRequest);
  
  UserRes queryUserByUserName(String userName);
  
}
