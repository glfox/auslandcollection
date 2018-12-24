package com.ausland.weixin.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ausland.weixin.model.db.ShouhouListFromExcel;

@Repository
public interface ShouhouListFromExcelRepository extends JpaRepository<ShouhouListFromExcel, String>{

 
	List<ShouhouListFromExcel> findByReceiverPhone(String receiverPhone);
	List<ShouhouListFromExcel> findByReceiverName(String receiverName);
	@Transactional
	void deleteByCreatedDateTimeBefore(Date date);
	@Transactional
	void deleteByBranName(String branName);
}
