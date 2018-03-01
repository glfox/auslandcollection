package com.ausland.weixin.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ausland.weixin.model.db.User;
import com.ausland.weixin.model.reqres.CreateUserReq;
import com.ausland.weixin.model.reqres.UserRes;
import com.ausland.weixin.model.ui.LoginForm;

public interface UserService {
  boolean validateUserNamePassword(String userName, String password);
  
  UserRes createUser(HttpServletRequest httpServletRequest, CreateUserReq createUserRequest);
  
  UserRes queryUserByName(HttpServletRequest httpServletRequest, String name);
  
  
  List<UserRes> queryUserByName(HttpServletRequest httpServletRequest, )
}
