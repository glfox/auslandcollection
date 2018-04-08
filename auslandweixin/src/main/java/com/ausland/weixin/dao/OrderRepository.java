package com.ausland.weixin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ausland.weixin.model.db.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

	 Order findById(Integer orderId);

 
}
