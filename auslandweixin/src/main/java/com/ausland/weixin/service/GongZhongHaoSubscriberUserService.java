package com.ausland.weixin.service;

import com.ausland.weixin.model.reqres.GongZhongHaoUserInfoRes;

public interface GongZhongHaoSubscriberUserService {

	GongZhongHaoUserInfoRes getWeChatUserInfo(String openId);
	
	
}
