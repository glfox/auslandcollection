package com.ausland.weixin.service.impl;

 
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ausland.weixin.model.db.User;
import com.ausland.weixin.model.reqres.CreateUserReq;
import com.ausland.weixin.model.reqres.GlobalRes;
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
	public boolean userNameExists(String userName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GlobalRes createUser(HttpServletRequest httpServletRequest, CreateUserReq createUserRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserRes queryUserBy(String userName, String userId, String email, String phoneNo) {
		// TODO Auto-generated method stub
		return null;
	}



}
