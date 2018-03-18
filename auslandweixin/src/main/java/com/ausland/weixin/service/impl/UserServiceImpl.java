package com.ausland.weixin.service.impl;

 
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.dao.UserRepository;
import com.ausland.weixin.model.db.User;
import com.ausland.weixin.model.reqres.Address;
import com.ausland.weixin.model.reqres.CreateUserReq;
import com.ausland.weixin.model.reqres.GlobalRes;
import com.ausland.weixin.model.reqres.UserRes;
import com.ausland.weixin.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails)userDetails).getUsername();
        }

        return null;
    }

    @Override
    public void autologin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            logger.debug(String.format("Auto login %s successfully!", username));
        }
    }
	
	@Override
	public String validateUserNamePassword(String userName, String password) {
		if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password))
		{
			return "用户名，密码不能为空";
		}
		User user = userRepository.findByUsername(userName);
		if(user == null)
			return "用户名不存在";
		if(!user.getStatus().equals(AuslandApplicationConstants.ACTIVE_USER_STATUS))
			return "用户账号已被锁定，请联系客服";
		if(!bCryptPasswordEncoder.matches(password, user.getPassword()))
		{
			return "用户名密码不匹配";
		}
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
		GlobalRes res = new GlobalRes();
		try
		{
			logger.debug("entered createUser with createUserRequest: "+createUserRequest);
			String ret = validateCreateUserRequest(createUserRequest);
			if(!StringUtils.isEmpty(ret)) 
			{
				res.setErrorDetails(ret);
				res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				return res;
			}
			User user = new User();
			user.setRole(AuslandApplicationConstants.STANDARD_USER_ROLE);
			user.setCreatedDateTime(new Date());
			user.setEmail(createUserRequest.getEmail());
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setPhoneNumber(createUserRequest.getPhoneNumber());
			user.setStatus(AuslandApplicationConstants.ACTIVE_USER_STATUS);
			user.setUsername(createUserRequest.getUserName());
			user.setAccountBalance(new BigDecimal(0));
			userRepository.save(user);
			res.setStatus(AuslandApplicationConstants.STATUS_OK);
			return res;
		}
		catch(Exception e)
		{
			logger.debug("caught exception during create user:"+e.getMessage());
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			res.setErrorDetails("创建用户失败："+e.getMessage());
		}
		
		return res;
	}
	
	
 
	private String validateCreateUserRequest(CreateUserReq createUserRequest)
	{
		if(createUserRequest == null || StringUtils.isEmpty(createUserRequest.getUserName()) || StringUtils.isEmpty(createUserRequest.getPassword()))
		{
			return "用户名或者密码为空";
		}
		User u = userRepository.findByUsername(createUserRequest.getUserName());
		if(u != null)
		{
			return "该用户名已经存在";
		}
		return null;
	}
	
	@Override
	public UserRes queryUserByUserName(String userName) {
		if(userName == null)
		{
			return null;
		}
		User u = userRepository.findByUsername(userName);
		if(u != null)
		{
			UserRes userRes = new UserRes();
			Address address = new Address();
			address.setAddress(u.getDefaultReceiverAddress());
			address.setPhoneNumber(u.getDeafaultReceiverPhone());
			address.setUserName(u.getDefaultReceiverName());
			userRes.setDefaultAddress(address);
			userRes.setEmail(u.getEmail());
			userRes.setPhoneNumber(u.getPhoneNumber());
			userRes.setRole(u.getRole());
			userRes.setStatus(u.getStatus());
			userRes.setUserName(userName);
			userRes.setUserId(u.getId());
			BigDecimal d = u.getAccountBalance();
			userRes.setAccountBalance(d.toPlainString());
			return userRes;
		}
		return null;
	}

	@Override
	public GlobalRes resetUserStatus(String userName, String userStatus) {
		GlobalRes res = new GlobalRes();
		if(AuslandApplicationConstants.ACTIVE_USER_STATUS.equalsIgnoreCase(userStatus) || 
	       AuslandApplicationConstants.LOCKED_USER_STATUS.equalsIgnoreCase(userStatus) || 
		   AuslandApplicationConstants.PENDING_USER_STATUS.equalsIgnoreCase(userStatus))
		{
			try
			{
				User u = userRepository.findByUsername(userName);
				if(u == null)
				{
					res.setErrorDetails("该用户名不存在");
					res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
					return res;
				}
				
				u.setStatus(userStatus);
				userRepository.save(u);
				res.setStatus(AuslandApplicationConstants.STATUS_OK);
				return res;
			}
			catch(Exception e)
			{
				res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				res.setErrorDetails("修改用户状态失败："+e.getMessage());
			}
			return res;
		}
		else
		{
			res.setErrorDetails("用户状态不合法");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
	}

	@Override
	public GlobalRes resetUserPassword(String userName, String userPassword) {
		// TODO Auto-generated method stub
		return null;
	}


}
