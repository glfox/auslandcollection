package com.ausland.weixin.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ausland.weixin.model.reqres.QueryStockRes;
import com.ausland.weixin.service.QueryStockService;

@RestController
@RequestMapping(value = "/query/stock")
public class QueryStockController {

    private static final Logger logger = LoggerFactory.getLogger(QueryZhongHuanController.class);
    
	@Autowired
	private QueryStockService queryStockService; 

	@RequestMapping(value = "/productid", method = RequestMethod.GET)
	public QueryStockRes queryStockByProductId(@RequestParam(name="productId", required = true) String productId,
			                                   @RequestParam(name="stockStatus", required = false) String stockStatus,
			                                   HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("entered QueryStockController.queryStockByProductId with productId:"+productId);
		ArrayList<String> list = new ArrayList<String>();
		list.add(productId);
		return queryStockService.queryStockByProductIds(list,stockStatus);
	}
	
	
	@RequestMapping(value = "/brand", method = RequestMethod.GET)
	public QueryStockRes queryStockByBrandName(@RequestParam(name="brandname", required = true) String brandName,
			                                   @RequestParam(name="stockStatus", required = false) String stockStatus,
			                                   HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("entered QueryStockController.queryStockByBrandName with brandName:"+brandName);
		return queryStockService.queryStockByBrandName(brandName, stockStatus);
	}
	
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public QueryStockRes queryAllStockInfo(@RequestParam(name="stockStatus", required = false) String stockStatus,
			                               HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("entered QueryStockController.queryAllStockInfo");
		return queryStockService.queryAllStockInfo(stockStatus);
	}
	
}
