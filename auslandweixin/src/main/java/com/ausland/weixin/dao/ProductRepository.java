package com.ausland.weixin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ausland.weixin.model.db.Product;

public interface ProductRepository extends JpaRepository<Product, String>{

	List<Product> findByProductId(String productId);
	List<Product> findByProductIdAndStockStatus(String productId, String stockStatus);
	List<Product> findByProductIdIn(List<String> productIds);
	List<Product> findByProductIdInAndStockStatus(List<String> productIds, String stockStatus);
	List<Product> findByBrandLike(String brand);
	
	List<Product> findByStockStatus(String stockStatus);
	List<Product> findByBrandLikeAndStockStatus(String brand , String stockStatus);
}
