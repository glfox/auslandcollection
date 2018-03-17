package com.ausland.weixin.service.impl;

 
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.dao.UserRepository;
import com.ausland.weixin.model.db.User;
import com.ausland.weixin.model.reqres.CreateUserReq;
import com.ausland.weixin.model.reqres.GlobalRes;
import com.ausland.weixin.model.reqres.UserRes;
import com.ausland.weixin.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public String validateUserNamePassword(String userName, String password) {
		if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password))
		{
			return "用户名，密码不能为空";
		}
		User user = userRepository.findByUsername(userName);
		if(user == null)
			return "用户名不存在";
		if(!password.equals(user.getPassword()))
			return "密码不正确";
		if(!user.getStatus().equals(AuslandApplicationConstants.ACTIVE_USER_STATUS))
			return "用户账号已被锁定，请联系客服";
		return null;
	}

	@Override
	public boolean userNameExists(String userName) {
		if(StringUtils.isEmpty(userName))
		{
			return true;
		}
		User user = userRepository.findByUsername(userName);
		if(user == null)
		{
			return false;
		}
		return true;
	}

	@Override
	public GlobalRes createUser(CreateUserReq createUserRequest) {
		logger.debug("entered createUser with createUserRequest: "+createUserRequest.toString());
		/*if(createUserRequest == null)*/
		return null;
	}
/*
	String validateCreateUserRequest()*/
	@Override
	public UserRes queryUserByUserName(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

}
