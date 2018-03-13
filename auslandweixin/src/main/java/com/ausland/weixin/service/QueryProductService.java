package com.ausland.weixin.service;

import java.util.List;

import com.ausland.weixin.model.reqres.QueryProductRes;

public interface QueryProductService {

	QueryProductRes queryByProductIds(List<String> productIds);
	
	QueryProductRes queryByProductId(String productId);

	QueryProductRes queryByBrandName(String brandName);
	
	QueryProductRes queryByProductIdOrProductNameMatchingLike(String matchingString);
	
	QueryProductRes queryAll(Integer pageNo, Integer pageSize);
	
	List<String> getAllBrand();
	
	List<String> getAllCategory();
}
