package com.ausland.weixin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ausland.weixin.model.db.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String>{

	 Brand findByBrandName(String brandName);
	 
}
