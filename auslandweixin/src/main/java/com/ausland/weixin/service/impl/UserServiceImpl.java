package com.ausland.weixin.service.impl;

 
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ausland.weixin.model.db.User;
import com.ausland.weixin.model.reqres.CreateUserReq;
import com.ausland.weixin.model.reqres.UserRes;
import com.ausland.weixin.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public boolean validateUserNamePassword(String userName, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserRes createUser(HttpServletRequest httpServletRequest, CreateUserReq createUserRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserRes queryUserByName(HttpServletRequest httpServletRequest, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	 

}
