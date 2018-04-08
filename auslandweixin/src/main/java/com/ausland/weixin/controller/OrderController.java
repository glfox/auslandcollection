package com.ausland.weixin.controller;

import java.io.IOException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.reqres.CreateOrderDetailsRes;
import com.ausland.weixin.model.reqres.CreateOrderReq;
import com.ausland.weixin.model.reqres.QueryOrderDetailsRes;
import com.ausland.weixin.model.reqres.CreateProductReq;
import com.ausland.weixin.model.reqres.CreateProductRes;
import com.ausland.weixin.model.reqres.GlobalListRes;
import com.ausland.weixin.model.reqres.GlobalRes;
import com.ausland.weixin.model.reqres.UpdateProductStockReq;
import com.ausland.weixin.service.OrderService;
import com.ausland.weixin.service.ProductService;
import com.ausland.weixin.util.CookieUtil;
import com.ausland.weixin.util.CustomCookie;
import com.ausland.weixin.util.ImageUtil;

@RestController
@MultipartConfig
@RequestMapping(value = "/provisioning/order")
public class OrderController {
	
private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    
	@Autowired
	private OrderService orderService; 
	
	@Autowired
	private CookieUtil cookieUtil;
	@RequestMapping(value = "/queryorder", method = RequestMethod.GET)
	public QueryOrderDetailsRes queryOrder(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "orderStatus", required = false) String orderStatus,
			@CookieValue(value=AuslandApplicationConstants.COOKIE_NAME, required=false)String cookieValue)
	{
		QueryOrderDetailsRes res = new QueryOrderDetailsRes();
		try
		{
			CustomCookie cc = cookieUtil.validateCookieValue(cookieValue);
			if(cc == null || cc.getUserName() == null || cc.getRole() == null)
			{
				 res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				 res.setErrorDetails("没有找到用户信息，不能查询订单");
				 return res;
			}
			return orderService.queryOrderBy(cc, orderStatus, fromDate, toDate);
		}
		catch(Exception e)
		{
			 res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			 res.setErrorDetails("查询订单失败："+e.getMessage());
		}
		return res;
	}
	
	@RequestMapping(value = "/deleteorder", method = RequestMethod.DELETE)
	public GlobalRes deleteOrder(@RequestParam(value = "orderId", required = true) String orderId,
		                         @CookieValue(value=AuslandApplicationConstants.COOKIE_NAME, required=false)String cookieValue)
	{
		 GlobalRes res = new GlobalRes();
		 try
		 {
			 CustomCookie cc = cookieUtil.validateCookieValue(cookieValue);
			 if(cc == null || cc.getUserName() == null || cc.getRole() == null)
			 {
				 res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				 res.setErrorDetails("没有找到用户信息，不能删除");
				 return res;
			 }
			 if(AuslandApplicationConstants.STANDARD_USER_ROLE.equalsIgnoreCase(cc.getRole()))
			 {
				 res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				 res.setErrorDetails("该登陆账号不具有删除订单权限");
				 return res;
			 }
			 return orderService.deleteOrder(cc, orderId);
		 }
		 catch(Exception e)
		 {
			 res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			 res.setErrorDetails("删除订单失败："+e.getMessage());
		 }
		 return res;
	}
  
	@RequestMapping(value = "/createorder", method = RequestMethod.POST)
	public CreateOrderDetailsRes createOrder(@RequestBody(required = true) CreateOrderReq req,
			                                 @CookieValue(value=AuslandApplicationConstants.COOKIE_NAME, required=false)String cookieValue,
			                                 HttpServletRequest httpServletRequest,
			                                 HttpServletResponse httpServletResponse)   
	{
		logger.debug("entered createProduct with CreateProductReq:"+req.toString());
		CreateOrderDetailsRes res = new CreateOrderDetailsRes();
		try
		 {
			 CustomCookie cc = cookieUtil.validateCookieValue(cookieValue);
			 if(cc == null || cc.getUserName() == null || cc.getRole() == null)
			 {
				 res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				 res.setErrorDetails("没有找到用户信息，不能创建");
				 return res;
			 }
			 
			 return orderService.createOrder(cc, req);
		 }
		 catch(Exception e)
		 {
			 res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			 res.setErrorDetails("创建订单失败："+e.getMessage());
		 }
		 return res;
	}

	@RequestMapping(value = "/bulkuploadorder", method = RequestMethod.POST)
	public GlobalListRes uploadOrderFromExcel(@RequestPart(required = true)MultipartFile excelFile,
			                                  HttpServletRequest httpServletRequest,
			                                  @CookieValue(value=AuslandApplicationConstants.COOKIE_NAME, required=false)String cookieValue) throws IOException 
	{
		logger.debug("entered uploadOrderFromExcel");
		GlobalListRes res = new GlobalListRes();
		 try
		 {
			 CustomCookie cc = cookieUtil.validateCookieValue(cookieValue);
			 if(cc == null || cc.getUserName() == null || cc.getRole() == null)
			 {
				 res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				 res.setErrorDetails("没有找到用户信息，不能批量上传");
				 return res;
			 }
			 if(AuslandApplicationConstants.STANDARD_USER_ROLE.equalsIgnoreCase(cc.getRole()))
			 {
				 res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				 res.setErrorDetails("该登陆账号不具有批量上传订单权限");
				 return res;
			 }
			 return orderService.uploadOrderFromExcel(cc, excelFile);
		 }
		 catch(Exception e)
		 {
			 res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			 res.setErrorDetails("批量上传订单失败："+e.getMessage());
		 }
		 return res;
		 
	} 

}
