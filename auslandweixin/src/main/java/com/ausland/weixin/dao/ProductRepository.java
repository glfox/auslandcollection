package com.ausland.weixin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ausland.weixin.model.db.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

	List<Product> findByProductId(String productId);
	List<Product> findByProductIdAndStockStatus(String productId, String stockStatus);
	List<Product> findByProductIdIn(List<String> productIds);
	List<Product> findByProductIdInAndStockStatus(List<String> productIds, String stockStatus);
	List<Product> findByBrandLike(String brand);
	
	List<Product> findByStockStatus(String stockStatus);
	List<Product> findByBrandLikeAndStockStatus(String brand , String stockStatus);
}
