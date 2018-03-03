package com.ausland.weixin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
import com.ausland.weixin.service.QueryStockService;
import com.ausland.weixin.service.QueryZhongHuanService;

@RestController
@RequestMapping(value = "/query/stock")
public class QueryStockController {

	
private static final Logger logger = LoggerFactory.getLogger(QueryZhongHuanController.class);
    
	@Autowired
	private QueryStockService queryStockService; 

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public QueryStockRes queryZhongHuanLastThreeMonthbyPhoneNo(@RequestParam(name="phone", required = true) String phoneNo, 
			@RequestParam(name="details", required = true) Boolean fetchDetails, HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("entered QueryZhongHuanController.queryZhongHuanLastThreeMonthbyPhoneNo with phoneno:"+phoneNo+"; fetch details:"+fetchDetails);
		return QueryStockService.queryZhongHuanLastThreeMonthbyPhoneNo(phoneNo, fetchDetails);
	}
}
