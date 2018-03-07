package com.ausland.weixin.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ausland.weixin.model.db.GongZhongHaoUser;

@Repository
public interface GongZhongHaoUserRepository extends CrudRepository<GongZhongHaoUser, String>{

	List<GongZhongHaoUser> findByOpenid(String openid);
	
}