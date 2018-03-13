package com.ausland.weixin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ausland.weixin.model.db.ProductStock;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Integer>{

	 List<ProductStock> findByProductId(String productId);
	 
	 void deleteByProductId(String productId);
}
