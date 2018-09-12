package com.ausland.weixin.service;

import java.util.List;

import com.ausland.weixin.model.difou.GetDifouOrderListRes;
import com.ausland.weixin.model.difou.GetStockListRes;
import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
 
public interface DifouService {
    QueryZhongHuanLastThreeMonthByPhoneNoRes queryDifouLastThreeMonth(String nameOrPhoneNo);
	GetStockListRes getStockList(String productId);
	List<String> getAllProductIds();

	GetDifouOrderListRes getLastThreeMonthOrders();
}
