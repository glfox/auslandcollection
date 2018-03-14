package com.ausland.weixin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.reqres.QueryProductRes;
import com.ausland.weixin.service.QueryProductService;

@RestController
@RequestMapping(value = "/query/product")
public class QueryProductController {
	
private static final Logger logger = LoggerFactory.getLogger(QueryProductController.class);
    
	@Autowired
	private QueryProductService queryProductService; 
	
	@RequestMapping(value = "/getproductby", method = RequestMethod.GET)
	public QueryProductRes queryProductBy(@RequestParam(name="pageSize", required = true) Integer pageSize,
									      @RequestParam(name="pageNo", required = true) Integer pageNo,
									      @RequestParam(name="matchingstr", required = false) String mathingStr,
			                              @RequestParam(name="brands", required = false) String brandNames,
			                              @RequestParam(name="productIds", required = true) String productIds,
			                              HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered queryByMatchingStr with mathingStr:"+mathingStr+";brandnames:"+brandNames+";productIds:"+productIds+";pageSize:"+pageSize+";pageNo:"+pageNo); 
		
		return queryProductService.queryProductBy(pageNo, pageSize, brandNames, mathingStr, productIds);
	}
	
	@RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
	public QueryProductRes queryProductByProductId(@RequestParam(name="productId", required = true) String productId,
			                                   HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered queryProductByProductId "); 
		return queryProductService.queryByProductId(productId);
	}
	
	
	@RequestMapping(value = "/getallbrand", method = RequestMethod.GET)
	public List<String> getAllBrand(HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered getAllBrand "); 
		return queryProductService.getAllBrand();
	}
	
	@RequestMapping(value = "/getallcategory", method = RequestMethod.GET)
	public List<String> getAllCategory(HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered getAllCategory "); 
		return queryProductService.getAllCategory();
	}
	
	@RequestMapping(value = "/getproductidlistby", method = RequestMethod.GET)
	public List<String> getProductIdListBy(@RequestParam(name="brands", required = false) String brandNames,
			                               @RequestParam(name="matchingstr", required = false) String matchingStr,
	                                       HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered getAllCategory "); 
		return queryProductService.getProductIdListBy(brandNames, matchingStr);
	}
	
	 
}
