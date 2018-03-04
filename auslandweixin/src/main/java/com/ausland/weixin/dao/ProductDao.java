package com.ausland.weixin.dao;

import java.util.List;

import com.ausland.weixin.model.reqres.StockInfo;

public interface ProductDao {
	   List<String> getMatchingProductIds(String productId);
	
	   List<String> getMatchingBrandNames(String brandName);

	   List<StockInfo> getStockInfoByProductionId(String productId,String matchingStockStatus);
	   List<StockInfo> getStockInfoByProductionIds(List<String> productIdList,String matchingStockStatus);
	
	   List<StockInfo> getStockInfoByBrandNames(List<String> brandNameList,String matchingStockStatus);
	   List<StockInfo> getStockInfoByBrandName(String brandName,String matchingStockStatus);
}
