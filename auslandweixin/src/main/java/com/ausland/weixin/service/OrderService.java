package com.ausland.weixin.service;

import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.model.reqres.CreateOrderDetailsRes;
import com.ausland.weixin.model.reqres.CreateOrderReq;
import com.ausland.weixin.model.reqres.QueryOrderDetailsRes;
import com.ausland.weixin.model.reqres.GlobalListRes;
import com.ausland.weixin.model.reqres.GlobalRes;
import com.ausland.weixin.model.reqres.PurchaseItemReq;
import com.ausland.weixin.util.CustomCookie;

public interface OrderService {

	GlobalRes deleteOrder(CustomCookie customCookie, String orderId);
	
	QueryOrderDetailsRes queryOrderBy(CustomCookie customCookie, String orderStatus, String fromDate, String toDate);
	
	CreateOrderDetailsRes createOrder(CustomCookie customCookie, CreateOrderReq req);
	
	GlobalRes updatePurchaseItemToOrder(CustomCookie customCookie, String orderId, Integer purchaseItemId, PurchaseItemReq req); 
	
	GlobalRes deletePurchaseItemFromOrder(CustomCookie customCookie, String orderId, Integer purchaseItemId);
	
	GlobalRes addPurchaseItemToOrder(CustomCookie customCookie, String orderId, PurchaseItemReq req); 
	 
	GlobalListRes uploadOrderFromExcel(CustomCookie customCookie, MultipartFile excelFile);
	
}
