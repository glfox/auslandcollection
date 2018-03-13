package com.ausland.weixin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ausland.weixin.model.db.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{

	 Category findByCategoryName(String categoryName);
}
