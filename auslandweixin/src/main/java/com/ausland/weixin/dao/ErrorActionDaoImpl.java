package com.ausland.weixin.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ausland.weixin.model.db.ErrorAction;
import com.ausland.weixin.util.ValidationUtil;

@Repository
public class ErrorActionDaoImpl implements ErrorActionDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ValidationUtil validationUtil;
	
	private static final Logger logger = LoggerFactory.getLogger(PackageDaoImpl.class);
	
	@Override
	public void insertErrorAction(ErrorAction ea) {
		 
		if(ea == null)
			return;
		try
		{
			String sql = "insert into erroraction(errorModule, errorAction, errorDetails, createdDateTime, status) values(?,?,?,?,?)";
			jdbcTemplate.update(sql,ea.getErrorModule(), ea.getErrorAction(), ea.getErrorDetails().substring(0, 2048), validationUtil.getClass(), ea.getStatus());
		}
		catch(Exception e)
		{
			logger.error("caught exception during insertErrorAction:"+e.getMessage());
		}
	}

}
