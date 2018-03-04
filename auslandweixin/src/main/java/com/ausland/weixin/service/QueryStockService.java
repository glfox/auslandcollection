package com.ausland.weixin.service;

import java.util.List;

import com.ausland.weixin.model.reqres.QueryStockRes;

public interface QueryStockService {

	QueryStockRes queryStockByProductIds(List<String> productIds, String matchingStockStatus);

	QueryStockRes queryStockByBrandName(String brandName, String matchingStockStatus);
	
	QueryStockRes queryAllStockInfo(String matchingStockStatus);
}
