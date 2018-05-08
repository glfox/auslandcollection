package com.ausland.weixin.dao;

import java.util.List;
import java.util.Set;

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
	
	Page<Product> findByProductIdIn(Pageable pageable, List<String> productIds);
	
	Page<Product> findByProductIdLikeOrProductNameLike(Pageable pageable,String productId, String productName);
	
	Page<Product> findByBrandIn(Pageable pageable, List<String> brands);
	
	@Query(value="select p.productId from Product p where p.productId in :productIdSet")
	List<String> findProductIdByProductIdIn( @Param("productIdSet")Set<String> productIdSet);
	
	Page<Product> findByProductIdLikeOrProductNameLikeAndBrandIn(Pageable pageable, String productId,  String productName, List<String> brands);
	
	@Query(value="select p.productId from Product p where p.productId like %:matchingStr% or p.productName like %:matchingStr%")
	List<String> findProductIdsByMatchingString(@Param("matchingStr")String matchingStr);
	
	@Query(value="select p.productId from Product p where p.brand in :brands")
	List<String> findProductIdsByBrands(@Param("brands")List<String> brands);
	
	@Query(value="select p.productId from Product p where p.productId like %:matchingStr% or p.productName like %:matchingStr% and p.brand in :brands")
	List<String> findProductIdsByMatchingStringAndBrands(@Param("matchingStr")String matchingStr, @Param("brands")List<String> brands);
	
	Page<Product> findAll(Pageable pageable);
	
	@Query(value="select p.productId from Product p")
	List<String> findProductIdsAll();

}
