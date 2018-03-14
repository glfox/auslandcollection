package com.ausland.weixin.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ausland.weixin.model.db.GongZhongHaoUser;

@Repository
public interface GongZhongHaoUserRepository extends CrudRepository<GongZhongHaoUser, String>{

	GongZhongHaoUser findByOpenid(String openid);
	
}