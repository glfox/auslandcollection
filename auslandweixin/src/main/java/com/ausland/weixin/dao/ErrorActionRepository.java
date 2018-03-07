package com.ausland.weixin.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ausland.weixin.model.db.ErrorAction;

@Repository
public interface ErrorActionRepository extends CrudRepository<ErrorAction, Integer>{

	
}
