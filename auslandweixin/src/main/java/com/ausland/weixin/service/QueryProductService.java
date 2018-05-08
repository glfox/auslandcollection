package com.ausland.weixin.service;

import java.util.List;

import com.ausland.weixin.model.reqres.ProductIdsReq;
import com.ausland.weixin.model.reqres.QueryProductRes;

public interface QueryProductService {

	QueryProductRes queryByProductId(String productId);

	QueryProductRes queryProductBy(Integer pageNo, Integer pageSize, String brandNames, String matchingString, ProductIdsReq productIds);
	
	List<String> getAllBrand();
	
	List<String> getAllCategory();
	
	List<String> getProductIdListBy(String brandNames, String matchingString);
}
