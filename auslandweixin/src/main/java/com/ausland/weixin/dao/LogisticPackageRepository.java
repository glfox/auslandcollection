package com.ausland.weixin.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ausland.weixin.model.db.LogisticPackage;

public interface LogisticPackageRepository extends JpaRepository<LogisticPackage, String>{

 
	List<LogisticPackage> findByCreatedSrc(String createdSrc);
	List<LogisticPackage> findByReceiverPhoneAndCreatedDateTimeBetween(String receiverPhone, Date fromDate, Date toDate);
	
	List<LogisticPackage> findByCreatedDateTimeBetween(Date fromDate, Date toDate);
	
}
