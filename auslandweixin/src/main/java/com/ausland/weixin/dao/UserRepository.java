package com.ausland.weixin.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ausland.weixin.model.db.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	
}
