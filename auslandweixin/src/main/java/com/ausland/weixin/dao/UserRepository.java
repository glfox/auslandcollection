package com.ausland.weixin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ausland.weixin.model.db.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	
}
