package com.ausland.weixin.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ausland.weixin.model.db.OrderListFromExcel;

@Repository
public interface OrderListFromExcelRepository extends JpaRepository<OrderListFromExcel, String>{

 
	List<OrderListFromExcel> findByReceiverPhone(String receiverPhone);
	List<OrderListFromExcel> findByReceiverName(String receiverName);
	@Transactional
	List<OrderListFromExcel> deleteByCreatedDateTimeBefore(Date date);

}
