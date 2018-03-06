package com.ausland.weixin.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ausland.weixin.model.db.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

	
}
