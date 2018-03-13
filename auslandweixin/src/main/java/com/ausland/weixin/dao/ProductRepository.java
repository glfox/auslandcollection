package com.ausland.weixin.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ausland.weixin.model.db.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>{

	Product  findByProductId(String productId);
	
	List<Product> findByProductIdIn(List<String> productIds);
	
	List<Product> findByProductIdLike(String productId);
	
	List<Product> findByBrand(String brand);
	
	@Query(value="select p from Product p where p.productId like %:matchingStr% or p.productName like %:matchingStr%")
	List<Product> findByMatchingString(@Param("matchingStr")String matchingStr);
	
	Page<Product> findAll(Pageable pageable);
}
