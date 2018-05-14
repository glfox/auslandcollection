package com.ausland.weixin.service;

import java.util.List;

import com.ausland.weixin.model.difou.GetStockListRes;
 
public interface DifouService {
 
	GetStockListRes getStockList(String productId);
	List<String> getAllProductIds();
}
